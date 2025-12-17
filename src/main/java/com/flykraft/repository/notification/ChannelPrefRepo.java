package com.flykraft.repository.notification;

import com.flykraft.exception.ConstraintViolationException;
import com.flykraft.model.notification.ChannelPref;
import com.flykraft.repository.Repository;

import java.util.*;

public class ChannelPrefRepo implements Repository<Integer, ChannelPref> {
    private int nextId;
    private final Map<Integer, ChannelPref> channelPrefData;
    private final Map<String, Integer> constraintsMap;

    public ChannelPrefRepo() {
        this.nextId = 1;
        this.channelPrefData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
    }

    @Override
    public List<ChannelPref> findAll() {
        return channelPrefData.values().parallelStream().toList();
    }

    @Override
    public Optional<ChannelPref> findById(Integer id) {
        return Optional.ofNullable(channelPrefData.get(id));
    }

    @Override
    public ChannelPref save(ChannelPref entity) {
        validateConstraint(entity);
        if (entity.getChannelPrefId() == null) {
            entity.setChannelPrefId(nextId++);
        }
        channelPrefData.put(entity.getChannelPrefId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getChannelPrefId());
        return entity;
    }

    private void validateConstraint(ChannelPref entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getChannelPrefId())) {
            throw new ConstraintViolationException("Data Constraint Violated - Selected channel preference for the stakeholder already exists.");
        }
    }

    @Override
    public void deleteById(Integer id) {
        ChannelPref entity = channelPrefData.get(id);
        constraintsMap.remove(getConstraintId(entity));
        channelPrefData.remove(id);
    }

    public List<ChannelPref> findByStakeHolderId(Integer stakeHolderId) {
        List<ChannelPref> channelPrefs = new ArrayList<>();
        for (ChannelPref channelPref : channelPrefData.values()) {
            if (channelPref.getStakeHolderId().equals(stakeHolderId)) {
                channelPrefs.add(channelPref);
            }
        }
        return channelPrefs;
    }

    private String getConstraintId(ChannelPref entity) {
        return entity.getStakeHolderId() + String.valueOf(entity.getChannelId());
    }
}
