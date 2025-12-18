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
            return statusSubData.values().stream().map(this::clone).toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<StatusSub> findById(Integer id) {
        lock.readLock().lock();
        try {
            StatusSub statusSub = statusSubData.get(id);
            return statusSub == null ? Optional.empty() : Optional.of(clone(statusSub));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StatusSub save(StatusSub entity) {
        lock.writeLock().lock();
        try {
            StatusSub statusSub = clone(entity);
            if (statusSub.getStatusSubId() == null) {
                statusSub.setStatusSubId(nextId++);
            }
            validateConstraint(statusSub);
            statusSubData.put(statusSub.getStatusSubId(), statusSub);
            constraintsMap.put(getConstraintId(statusSub), statusSub.getStatusSubId());
            return clone(statusSub);
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
            return statusSubData.values()
                    .stream()
                    .filter(s -> s.getStakeHolderId().equals(stakeHolderId))
                    .map(this::clone)
                    .toList();
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

    private StatusSub clone(StatusSub entity) {
        StatusSub clone = new StatusSub(entity.getStakeHolderId(), entity.getOrderStatusId());
        clone.setStatusSubId(entity.getStatusSubId());
        return clone;
    }
}
