package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.StatusSub;
import com.flykraft.repository.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatusSubRepo implements Repository<Integer, StatusSub> {
    private int nextId;
    private final Map<Integer, StatusSub> statusSubData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public StatusSubRepo() {
        this.nextId = 1;
        this.statusSubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public List<StatusSub> findAll() {
        lock.readLock().lock();
        try {
            return statusSubData.values().stream().toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<StatusSub> findById(Integer id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(statusSubData.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StatusSub save(StatusSub entity) {
        lock.writeLock().lock();
        try {
            if (entity.getStatusSubId() == null) {
                entity.setStatusSubId(nextId++);
            }
            validateConstraint(entity);
            statusSubData.put(entity.getStatusSubId(), entity);
            constraintsMap.put(getConstraintId(entity), entity.getStatusSubId());
            return entity;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraint(StatusSub entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getStatusSubId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            StatusSub removedEntity = statusSubData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(getConstraintId(removedEntity));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<StatusSub> findByStakeHolderId(Integer stakeHolderId) {
        lock.readLock().lock();
        try {
            List<StatusSub> statusSubs = new ArrayList<>();
            for (StatusSub statusSub : statusSubData.values()) {
                if (statusSub.getStakeHolderId().equals(stakeHolderId)) {
                    statusSubs.add(statusSub);
                }
            }
            return statusSubs;
        } finally {
            lock.readLock().unlock();
        }
    }

    private String getConstraintId(StatusSub entity) {
        if (entity.getStakeHolderId() == null || entity.getOrderStatusId() == null) {
            throw new DataConstraintViolationException("StakeHolder Id and OrderStatus Id must not be null");
        }
        return entity.getStakeHolderId() + "&" + entity.getOrderStatusId();
    }
}
