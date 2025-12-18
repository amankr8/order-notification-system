package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeliveryPartnerRepo implements Repository<Integer, DeliveryPartner> {
    private int nextId;
    private final Map<Integer, DeliveryPartner> partnerData;
    private final ReadWriteLock lock;

    public DeliveryPartnerRepo() {
        this.nextId = 1;
        partnerData = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<DeliveryPartner> findById(Integer id) {
        lock.readLock().lock();
        try {
            DeliveryPartner partner = partnerData.get(id);
            return partner == null ? Optional.empty() : Optional.of(clone(partner));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public DeliveryPartner save(DeliveryPartner entity) {
        lock.writeLock().lock();
        try {
            DeliveryPartner partner = clone(entity);
            if (partner.getPartnerId() == null) {
                partner.setPartnerId(nextId++);
            }
            partnerData.put(partner.getPartnerId(), partner);
            return clone(partner);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            partnerData.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private DeliveryPartner clone(DeliveryPartner entity) {
        DeliveryPartner clone = new DeliveryPartner(entity.getPartnerName());
        clone.setPartnerId(entity.getPartnerId());
        clone.setStakeHolderId(entity.getStakeHolderId());
        clone.setStakeHolderName(entity.getStakeHolderName());
        clone.setStakeHolderCategoryId(entity.getStakeHolderCategoryId());
        clone.setOptedInForNotifications(entity.hasOptedInForNotifications());
        return clone;
    }
}
