package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.NotifyMsg;
import com.flykraft.repository.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NotifyMsgRepo implements Repository<Integer, NotifyMsg> {
    private int nextId;
    private final Map<Integer, NotifyMsg> notifyMsgData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public NotifyMsgRepo() {
        this.nextId = 1;
        this.notifyMsgData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<NotifyMsg> findById(Integer id) {
        lock.readLock().lock();
        try {
            NotifyMsg notifyMsg = notifyMsgData.get(id);
            return notifyMsg == null ? Optional.empty() : Optional.of(clone(notifyMsg));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public NotifyMsg save(NotifyMsg entity) {
        lock.writeLock().lock();
        try {
            NotifyMsg notifyMsg = clone(entity);
            validateConstraints(notifyMsg);
            if (notifyMsg.getNotifyMsgId() == null) {
                notifyMsg.setNotifyMsgId(nextId++);
            }
            notifyMsgData.put(notifyMsg.getNotifyMsgId(), notifyMsg);
            constraintsMap.putIfAbsent(getConstraintId(notifyMsg), notifyMsg.getNotifyMsgId());
            return clone(notifyMsg);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraints(NotifyMsg notifyMsg) {
        String constraintId = getConstraintId(notifyMsg);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(notifyMsg.getNotifyMsgId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            NotifyMsg removedEntity = notifyMsgData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(getConstraintId(removedEntity));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<NotifyMsg> findByCategoryId(Integer stakeHolderCategoryId) {
        lock.readLock().lock();
        try {
            return notifyMsgData.values()
                    .stream()
                    .filter(m -> m.getStakeHolderCategoryId().equals(stakeHolderCategoryId))
                    .map(this::clone)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    private String getConstraintId(NotifyMsg entity) {
        if (entity.getStakeHolderCategoryId() == null || entity.getOrderStatusId() == null) {
            throw new DataConstraintViolationException("StakeHolder Category Id and Order Status Id must not be null");
        }
        return entity.getStakeHolderCategoryId() + "&" + entity.getOrderStatusId();
    }

    private NotifyMsg clone(NotifyMsg entity) {
        NotifyMsg clone = new NotifyMsg(entity.getStakeHolderCategoryId(), entity.getOrderStatusId(), entity.getMessage());
        clone.setNotifyMsgId(entity.getNotifyMsgId());
        return clone;
    }
}
