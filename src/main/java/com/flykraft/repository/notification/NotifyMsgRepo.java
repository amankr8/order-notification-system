package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.ConstraintViolationException;
import com.flykraft.model.notification.NotifyMsg;
import com.flykraft.repository.Repository;

import java.util.*;

public class NotifyMsgRepo implements Repository<Integer, NotifyMsg> {
    private int nextId;
    private final Map<Integer, NotifyMsg> notifyMsgData;
    private final Map<String, Integer> constraintsMap;

    public NotifyMsgRepo() {
        this.nextId = 1;
        this.notifyMsgData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
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
        validateConstraint(entity);
        if (entity.getNotifyMsgId() == null) {
            entity.setNotifyMsgId(nextId++);
        }
        notifyMsgData.put(entity.getNotifyMsgId(), entity);
        constraintsMap.putIfAbsent(getConstraintId(entity), entity.getNotifyMsgId());
        return entity;
    }

    private void validateConstraint(NotifyMsg entity) {
        String constraintId = getConstraintId(entity);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(entity.getNotifyMsgId())) {
            throw new ConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
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

    private String getConstraintId(NotifyMsg entity) {
        return entity.getStakeHolderCategoryId() + String.valueOf(entity.getOrderStatusId());
    }
}
