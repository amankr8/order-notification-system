package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.ConstraintViolationException;
import com.flykraft.model.notification.StatusSub;
import com.flykraft.repository.Repository;

import java.util.*;

public class StatusPrefRepo implements Repository<Integer, StatusSub> {
    private int nextId;
    private final Map<Integer, StatusSub> statusPrefData;
    private final Map<String, Integer> constraintsMap;

    public StatusPrefRepo() {
        this.nextId = 1;
        this.statusPrefData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<StatusSub> findAll() {
        return statusPrefData.values().parallelStream().toList();
    }

    @Override
    public Optional<StatusSub> findById(Integer id) {
        return Optional.ofNullable(statusPrefData.get(id));
    }

    @Override
    public StatusSub save(StatusSub entity) {
        validateConstraint(entity);
        if (entity.getStatusSubId() == null) {
            entity.setStatusSubId(nextId++);
        }
        statusPrefData.put(entity.getStatusSubId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getStatusSubId());
        return entity;
    }

    private void validateConstraint(StatusSub entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getStatusSubId())) {
            throw new ConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        StatusSub entity = statusPrefData.get(id);
        constraintsMap.remove(getConstraintId(entity));
        statusPrefData.remove(id);
    }

    public List<StatusSub> findByStakeHolderId(Integer stakeHolderId) {
        List<StatusSub> statusSubs = new ArrayList<>();
        for (StatusSub statusSub : statusPrefData.values()) {
            if (statusSub.getStakeHolderId().equals(stakeHolderId)) {
                statusSubs.add(statusSub);
            }
        }
        return statusSubs;
    }

    private String getConstraintId(StatusSub entity) {
        return entity.getStakeHolderId() + String.valueOf(entity.getOrderStatusId());
    }
}
