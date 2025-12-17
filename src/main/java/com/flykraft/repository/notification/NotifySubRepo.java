package com.flykraft.repository.notification;

import com.flykraft.model.notification.NotifySub;
import com.flykraft.repository.Repository;

import java.util.*;

public class NotifySubRepo implements Repository<Integer, NotifySub> {
    private int nextId;
    private final Map<Integer, NotifySub> notifySubData;

    public NotifySubRepo() {
        this.nextId = 1;
        this.notifySubData = new HashMap<>();
    }

    @Override
    public List<NotifySub> findAll() {
        return notifySubData.values().parallelStream().toList();
    }

    @Override
    public Optional<NotifySub> findById(Integer id) {
        return Optional.ofNullable(notifySubData.get(id));
    }

    @Override
    public NotifySub save(NotifySub notifySub) {
        if (notifySub.getNotifySubId() == null) {
            notifySub.setNotifySubId(nextId++);
        }
        notifySubData.put(notifySub.getNotifySubId(), notifySub);
        return notifySub;
    }

    @Override
    public void deleteById(Integer id) {
        notifySubData.remove(id);
    }

    public List<NotifySub> findByOrderId(Integer orderId) {
        List<NotifySub> notifySubs = new ArrayList<>();
        for (NotifySub notifySub : notifySubData.values()) {
            if (notifySub.getOrderId().equals(orderId)) {
                notifySubs.add(notifySub);
            }
        }
        return notifySubs;
    }
}
