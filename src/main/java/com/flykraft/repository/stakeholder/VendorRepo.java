package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VendorRepo implements Repository<Integer, Vendor> {
    private int nextId;
    private final Map<Integer, Vendor> vendorData;

    public VendorRepo() {
        this.nextId = 1;
        this.vendorData = new HashMap<>();
    }

    @Override
    public List<Vendor> findAll() {
        return vendorData.values().parallelStream().toList();
    }

    @Override
    public Optional<Vendor> findById(Integer id) {
        return Optional.ofNullable(vendorData.get(id));
    }

    @Override
    public Vendor save(Vendor entity) {
        if (entity.getVendorId() == null) {
            entity.setVendorId(nextId++);
        }
        vendorData.put(entity.getVendorId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        vendorData.remove(id);
    }
}
