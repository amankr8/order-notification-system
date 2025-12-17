package com.flykraft.repository.notification;

import com.flykraft.model.notification.NotifyMsg;
import com.flykraft.repository.Repository;

import java.util.*;

public class NotifyMsgRepo implements Repository<Integer, NotifyMsg> {
    private int nextId;
    private final Map<Integer, NotifyMsg> notifyMsgData;

    public NotifyMsgRepo() {
        this.nextId = 1;
        this.notifyMsgData = new HashMap<>();
    }

    @Override
    public List<NotifyMsg> findAll() {
        return notifyMsgData.values().parallelStream().toList();
    }

    @Override
    public Optional<NotifyMsg> findById(Integer id) {
        return Optional.ofNullable(notifyMsgData.get(id));
    }

    @Override
    public NotifyMsg save(NotifyMsg entity) {
        if (entity.getNotifyMsgId() == null) {
            entity.setNotifyMsgId(nextId++);
        }
        notifyMsgData.put(entity.getNotifyMsgId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        notifyMsgData.remove(id);
    }

    public List<NotifyMsg> findByCategoryId(Integer stakeHolderCategoryId) {
        List<NotifyMsg> notifyMsgs = new ArrayList<>();
        for (NotifyMsg notifyMsg : notifyMsgData.values()) {
            if (notifyMsg.getStakeHolderCategoryId().equals(stakeHolderCategoryId)) {
                notifyMsgs.add(notifyMsg);
            }
        }
        return notifyMsgs;
    }
}
