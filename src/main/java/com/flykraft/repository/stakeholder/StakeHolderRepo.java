package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StakeHolderRepo implements Repository<Integer, StakeHolder> {
    private int nextId;
    private final Map<Integer, StakeHolder> stakeHolderData;
    private final ReadWriteLock lock;

    public StakeHolderRepo() {
        this.nextId = 1;
        this.stakeHolderData = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public List<StakeHolder> findAll() {
        lock.readLock().lock();
        try {
            return stakeHolderData.values().stream().map(this::clone).toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<StakeHolder> findById(Integer id) {
        lock.readLock().lock();
        try {
            StakeHolder stakeHolder = stakeHolderData.get(id);
            return stakeHolder == null ? Optional.empty() : Optional.of(clone(stakeHolder));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StakeHolder save(StakeHolder entity) {
        lock.writeLock().lock();
        try {
            StakeHolder stakeHolder = clone(entity);
            if (stakeHolder.getStakeHolderId() == null) {
                stakeHolder.setStakeHolderId(nextId++);
            }
            stakeHolderData.put(stakeHolder.getStakeHolderId(), stakeHolder);
            return clone(stakeHolder);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            stakeHolderData.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private StakeHolder clone(StakeHolder entity) {
        StakeHolder clone = new StakeHolder(entity.getStakeHolderName(), entity.getStakeHolderCategoryId());
        clone.setStakeHolderId(entity.getStakeHolderId());
        clone.setOptedInForNotifications(entity.hasOptedInForNotifications());
        return clone;
    }
}
