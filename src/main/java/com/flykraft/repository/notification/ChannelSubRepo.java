package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.ChannelSub;
import com.flykraft.repository.Repository;

import java.util.*;

public class ChannelSubRepo implements Repository<Integer, ChannelSub> {
    private int nextId;
    private final Map<Integer, ChannelSub> channelSubData;
    private final Map<String, Integer> constraintsMap;

    public ChannelSubRepo() {
        this.nextId = 1;
        this.channelSubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<ChannelSub> findAll() {
        return channelSubData.values().parallelStream().toList();
    }

    @Override
    public Optional<ChannelSub> findById(Integer id) {
        return Optional.ofNullable(channelSubData.get(id));
    }

    @Override
    public ChannelSub save(ChannelSub entity) {
        validateConstraint(entity);
        if (entity.getChannelSubId() == null) {
            entity.setChannelSubId(nextId++);
        }
        channelSubData.put(entity.getChannelSubId(), entity);
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
        ChannelSub entity = channelSubData.get(id);
        constraintsMap.remove(getConstraintId(entity));
        channelSubData.remove(id);
    }

    public List<ChannelSub> findByStakeHolderId(Integer stakeHolderId) {
        List<ChannelSub> channelSubs = new ArrayList<>();
        for (ChannelSub channelSub : channelSubData.values()) {
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
