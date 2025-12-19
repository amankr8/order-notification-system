package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VendorRepo implements Repository<Integer, Vendor> {
    private int nextId;
    private final Map<Integer, Vendor> vendorData;
    private final ReadWriteLock lock;

    public VendorRepo() {
        this.nextId = 1;
        this.vendorData = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<Vendor> findById(Integer id) {
        lock.readLock().lock();
        try {
            Vendor vendor = vendorData.get(id);
            return vendor == null ? Optional.empty() : Optional.of(clone(vendor));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Vendor save(Vendor entity) {
        lock.writeLock().lock();
        try {
            Vendor vendor = clone(entity);
            if (vendor.getVendorId() == null) {
                vendor.setVendorId(nextId++);
            }
            vendorData.put(vendor.getVendorId(), vendor);
            return clone(vendor);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            vendorData.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Vendor clone(Vendor entity) {
        Vendor clone = new Vendor(entity.getVendorName());
        clone.setVendorId(entity.getVendorId());
        clone.setStakeHolderId(entity.getStakeHolderId());
        clone.setStakeHolderName(entity.getStakeHolderName());
        clone.setStakeHolderCategoryId(entity.getStakeHolderCategoryId());
        return clone;
    }
}
