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
    private final Map<Integer, Integer> constraintsMap;
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
            if (notifySub.getId() == null) {
                notifySub.setId(nextId++);
            }
            notifySubData.put(notifySub.getId(), notifySub);
            constraintsMap.putIfAbsent(notifySub.getStakeHolderId(), notifySub.getId());
            return clone(notifySub);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraints(NotifySub notifySub) {
        Integer stakeHolderId = notifySub.getStakeHolderId();
        if (stakeHolderId == null) {
            throw new DataConstraintViolationException("StakeHolder Id must not be null");
        }
        if (constraintsMap.containsKey(stakeHolderId) && !constraintsMap.get(stakeHolderId).equals(notifySub.getId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            NotifySub removedEntity = notifySubData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(removedEntity.getStakeHolderId());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<NotifySub> findByStakeHolderId(Integer stakeHolderId) {
        lock.readLock().lock();
        try {
            return notifySubData.values()
                    .stream()
                    .filter(n -> n.getStakeHolderId().equals(stakeHolderId))
                    .findFirst()
                    .map(this::clone);
        } finally {
            lock.readLock().unlock();
        }
    }

    private NotifySub clone(NotifySub entity) {
        NotifySub clone = new NotifySub(entity.getStakeHolderId(), entity.getOptedInForNotifications());
        clone.setId(entity.getId());
        return clone;
    }
}
