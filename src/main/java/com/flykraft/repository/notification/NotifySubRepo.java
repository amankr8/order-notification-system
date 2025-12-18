package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.NotifySub;
import com.flykraft.repository.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NotifySubRepo implements Repository<Integer, NotifySub> {
    private int nextId;
    private final Map<Integer, NotifySub> notifySubData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public NotifySubRepo() {
        this.nextId = 1;
        this.notifySubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<NotifySub> findById(Integer id) {
        lock.readLock().lock();
        try {
            NotifySub notifySub = notifySubData.get(id);
            return notifySub == null ? Optional.empty() : Optional.of(clone(notifySub));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public NotifySub save(NotifySub entity) {
        lock.writeLock().lock();
        try {
            NotifySub notifySub = clone(entity);
            validateConstraints(notifySub);
            if (notifySub.getNotifySubId() == null) {
                notifySub.setNotifySubId(nextId++);
            }
            notifySubData.put(notifySub.getNotifySubId(), notifySub);
            constraintsMap.putIfAbsent(getConstraintId(notifySub), notifySub.getNotifySubId());
            return clone(notifySub);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraints(NotifySub notifySub) {
        String constraintId = getConstraintId(notifySub);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(notifySub.getNotifySubId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            NotifySub removedEntity = notifySubData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(getConstraintId(removedEntity));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<NotifySub> findByOrderId(Integer orderId) {
        lock.readLock().lock();
        try {
            return notifySubData.values()
                    .stream()
                    .filter(n -> n.getOrderId().equals(orderId))
                    .map(this::clone)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    private String getConstraintId(NotifySub entity) {
        if (entity.getStakeHolderId() == null || entity.getOrderId() == null) {
            throw new DataConstraintViolationException("StakeHolder Id and Order Id must not be null");
        }
        return entity.getStakeHolderId() + "&" + entity.getOrderId();
    }

    private NotifySub clone(NotifySub entity) {
        NotifySub clone = new NotifySub(entity.getStakeHolderId(), entity.getOrderId());
        clone.setNotifySubId(entity.getNotifySubId());
        return clone;
    }
}
