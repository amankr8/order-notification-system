package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.ConstraintViolationException;
import com.flykraft.model.notification.NotifySub;
import com.flykraft.repository.Repository;

import java.util.*;

public class NotifySubRepo implements Repository<Integer, NotifySub> {
    private int nextId;
    private final Map<Integer, NotifySub> notifySubData;
    private final Map<String, Integer> constraintsMap;

    public NotifySubRepo() {
        this.nextId = 1;
        this.notifySubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<NotifySub> findAll() {
        return notifySubData.values().parallelStream().toList();
    }

    @Override
    public Optional<NotifySub> findById(Integer id) {
        return Optional.ofNullable(notifySubData.get(id));
    }

    @Override
    public NotifySub save(NotifySub entity) {
        validateConstraint(entity);
        if (entity.getNotifySubId() == null) {
            entity.setNotifySubId(nextId++);
        }
        notifySubData.put(entity.getNotifySubId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getNotifySubId());
        return entity;
    }

    private void validateConstraint(NotifySub entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getNotifySubId())) {
            throw new ConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        notifySubData.remove(id);
    }

    public List<NotifySub> findByOrderId(Integer orderId) {
        List<NotifySub> notifySubs = new ArrayList<>();
        for (NotifySub notifySub : notifySubData.values()) {
            if (notifySub.getOrderId().equals(orderId)) {
                notifySubs.add(notifySub);
            }
        }
        return notifySubs;
    }

    private String getConstraintId(NotifySub entity) {
        return entity.getStakeHolderId() + String.valueOf(entity.getOrderId());
    }
}
