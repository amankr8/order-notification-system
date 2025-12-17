package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StakeHolderRepo implements Repository<Integer, StakeHolder> {
    private int nextId;
    private final Map<Integer, StakeHolder> stakeHolderData;

    public StakeHolderRepo() {
        this.nextId = 1;
        this.stakeHolderData = new HashMap<>();
    }

    @Override
    public List<StakeHolder> findAll() {
        return stakeHolderData.values().parallelStream().toList();
    }

    @Override
    public Optional<StakeHolder> findById(Integer id) {
        return Optional.ofNullable(stakeHolderData.get(id));
    }

    @Override
    public StakeHolder save(StakeHolder entity) {
        if (entity.getStakeHolderId() == null) {
            entity.setStakeHolderId(nextId++);
        }
        stakeHolderData.put(entity.getStakeHolderId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        stakeHolderData.remove(id);
    }
}
