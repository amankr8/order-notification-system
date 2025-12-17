package com.flykraft.repository.notification;

import com.flykraft.model.notification.ChannelPref;
import com.flykraft.repository.Repository;

import java.util.*;

public class ChannelPrefRepo implements Repository<Integer, ChannelPref> {
    private int nextId;
    private final Map<Integer, ChannelPref> channelPrefData;

    public ChannelPrefRepo() {
        this.nextId = 1;
        this.channelPrefData = new HashMap<>();
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
        if (entity.getChannelPrefId() == null) {
            entity.setChannelPrefId(nextId++);
        }
        channelPrefData.put(entity.getChannelPrefId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
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
}
