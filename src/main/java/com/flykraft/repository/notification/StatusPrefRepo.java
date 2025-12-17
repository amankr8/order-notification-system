package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.ConstraintViolationException;
import com.flykraft.model.notification.NotifyMsg;
import com.flykraft.model.notification.StatusPref;
import com.flykraft.repository.Repository;

import java.util.*;

public class StatusPrefRepo implements Repository<Integer, StatusPref> {
    private int nextId;
    private final Map<Integer, StatusPref> statusPrefData;
    private final Map<String, Integer> constraintsMap;

    public StatusPrefRepo() {
        this.nextId = 1;
        this.statusPrefData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<StatusPref> findAll() {
        return statusPrefData.values().parallelStream().toList();
    }

    @Override
    public Optional<StatusPref> findById(Integer id) {
        return Optional.ofNullable(statusPrefData.get(id));
    }

    @Override
    public StatusPref save(StatusPref entity) {
        validateConstraint(entity);
        if (entity.getStatusPrefId() == null) {
            entity.setStatusPrefId(nextId++);
        }
        statusPrefData.put(entity.getStatusPrefId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getStatusPrefId());
        return entity;
    }

    private void validateConstraint(StatusPref entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getStatusPrefId())) {
            throw new ConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        StatusPref entity = statusPrefData.get(id);
        constraintsMap.remove(getConstraintId(entity));
        statusPrefData.remove(id);
    }

    public List<StatusPref> findByStakeHolderId(Integer stakeHolderId) {
        List<StatusPref> statusPrefs = new ArrayList<>();
        for (StatusPref statusPref : statusPrefData.values()) {
            if (statusPref.getStakeHolderId().equals(stakeHolderId)) {
                statusPrefs.add(statusPref);
            }
        }
        return statusPrefs;
    }

    private String getConstraintId(StatusPref entity) {
        return entity.getStakeHolderId() + String.valueOf(entity.getOrderStatusId());
    }
}
