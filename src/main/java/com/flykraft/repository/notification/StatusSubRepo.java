package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.StatusSub;
import com.flykraft.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatusSubRepo implements Repository<Integer, StatusSub> {
    private final AtomicInteger nextId;
    private final Map<Integer, StatusSub> statusSubData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public StatusSubRepo() {
        this.nextId = new AtomicInteger(1);
        this.statusSubData = new ConcurrentHashMap<>();
        this.constraintsMap = new ConcurrentHashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public List<StatusSub> findAll() {
        lock.readLock().lock();
        try {
            return statusSubData.values().parallelStream().toList();
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
            validateConstraint(entity);
            if (entity.getStatusSubId() == null) {
                int id = nextId.getAndIncrement();
                entity.setStatusSubId(id);
            }
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
            StatusSub entity = statusSubData.get(id);
            constraintsMap.remove(getConstraintId(entity));
            statusSubData.remove(id);
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
        return entity.getStakeHolderId() + String.valueOf(entity.getOrderStatusId());
    }
}
