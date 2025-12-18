package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.ChannelSub;
import com.flykraft.repository.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChannelSubRepo implements Repository<Integer, ChannelSub> {
    private int nextId;
    private final Map<Integer, ChannelSub> channelSubData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public ChannelSubRepo() {
        this.nextId = 1;
        this.channelSubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<ChannelSub> findById(Integer id) {
        lock.readLock().lock();
        try {
            ChannelSub channelSub = channelSubData.get(id);
            return channelSub == null ? Optional.empty() : Optional.of(clone(channelSub));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ChannelSub save(ChannelSub entity) {
        lock.writeLock().lock();
        try {
            ChannelSub channelSub = clone(entity);
            if (channelSub.getChannelSubId() == null) {
                channelSub.setChannelSubId(nextId++);
            }
            validateConstraint(channelSub);
            channelSubData.put(channelSub.getChannelSubId(), channelSub);
            constraintsMap.putIfAbsent(getConstraintId(channelSub), channelSub.getChannelSubId());
            return clone(channelSub);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraint(ChannelSub entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getChannelSubId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            ChannelSub removedEntity = channelSubData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(getConstraintId(removedEntity));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<ChannelSub> findByStakeHolderId(Integer stakeHolderId) {
        lock.readLock().lock();
        try {
            return channelSubData.values()
                    .stream()
                    .filter(c -> c.getStakeHolderId().equals(stakeHolderId))
                    .map(this::clone)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    private String getConstraintId(ChannelSub entity) {
        if (entity.getStakeHolderId() == null || entity.getChannelId() == null) {
            throw new DataConstraintViolationException("StakeHolder Id and Channel Id must not be null");
        }
        return entity.getStakeHolderId() + "&" + entity.getChannelId();
    }

    private ChannelSub clone(ChannelSub entity) {
        ChannelSub clone = new ChannelSub(entity.getStakeHolderId(), entity.getChannelId());
        clone.setChannelSubId(entity.getChannelSubId());
        return clone;
    }
}
