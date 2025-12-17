package com.flykraft.repository.notification;

import com.flykraft.model.notification.StatusPref;
import com.flykraft.repository.Repository;

import java.util.*;

public class StatusPrefRepo implements Repository<Integer, StatusPref> {
    private int nextId;
    private final Map<Integer, StatusPref> statusPrefData;

    public StatusPrefRepo() {
        this.nextId = 1;
        this.statusPrefData = new HashMap<>();
    }

    @Override
    public List<StatusPref> findAll() {
        return statusPrefData.values().parallelStream().toList();
    }

    @Override
    public Optional<StatusPref> findById(Integer id) {
        return Optional.ofNullable(statusPrefData.get(id));
    }

    @Override
    public StatusPref save(StatusPref entity) {
        if (entity.getStatusPrefId() == null) {
            entity.setStatusPrefId(nextId++);
        }
        statusPrefData.put(entity.getStatusPrefId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        statusPrefData.remove(id);
    }

    public List<StatusPref> findByStakeHolderId(Integer stakeHolderId) {
        List<StatusPref> statusPrefs = new ArrayList<>();
        for (StatusPref statusPref : statusPrefData.values()) {
            if (statusPref.getStakeHolderId().equals(stakeHolderId)) {
                statusPrefs.add(statusPref);
            }
        }
        return statusPrefs;
    }
}
