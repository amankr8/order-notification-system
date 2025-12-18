package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.ChannelSub;
import com.flykraft.repository.Repository;

import java.util.*;

public class ChannelPrefRepo implements Repository<Integer, ChannelSub> {
    private int nextId;
    private final Map<Integer, ChannelSub> channelPrefData;
    private final Map<String, Integer> constraintsMap;

    public ChannelPrefRepo() {
        this.nextId = 1;
        this.channelPrefData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<ChannelSub> findAll() {
        return channelPrefData.values().parallelStream().toList();
    }

    @Override
    public Optional<ChannelSub> findById(Integer id) {
        return Optional.ofNullable(channelPrefData.get(id));
    }

    @Override
    public ChannelSub save(ChannelSub entity) {
        validateConstraint(entity);
        if (entity.getChannelSubId() == null) {
            entity.setChannelSubId(nextId++);
        }
        channelPrefData.put(entity.getChannelSubId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getChannelSubId());
        return entity;
    }

    private void validateConstraint(ChannelSub entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getChannelSubId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        ChannelSub entity = channelPrefData.get(id);
        constraintsMap.remove(getConstraintId(entity));
        channelPrefData.remove(id);
    }

    public List<ChannelSub> findByStakeHolderId(Integer stakeHolderId) {
        List<ChannelSub> channelSubs = new ArrayList<>();
        for (ChannelSub channelSub : channelPrefData.values()) {
            if (channelSub.getStakeHolderId().equals(stakeHolderId)) {
                channelSubs.add(channelSub);
            }
        }
        return channelSubs;
    }

    private String getConstraintId(ChannelSub entity) {
        return entity.getStakeHolderId() + String.valueOf(entity.getChannelId());
    }
}
