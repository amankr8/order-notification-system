package com.flykraft.repository.notification;

import com.flykraft.model.notification.NotifySub;
import com.flykraft.repository.Repository;

import java.util.Optional;

public class NotifySubRepo implements Repository<Integer, NotifySub> {
    @Override
    public Optional<NotifySub> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public NotifySub save(NotifySub entity) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
