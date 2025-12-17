package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PartnerRepo implements Repository<Integer, DeliveryPartner> {
    private int nextId;
    private final Map<Integer, DeliveryPartner> partnerData;

    public PartnerRepo() {
        this.nextId = 1;
        partnerData = new HashMap<>();
    }

    @Override
    public List<DeliveryPartner> findAll() {
        return partnerData.values().parallelStream().toList();
    }

    @Override
    public Optional<DeliveryPartner> findById(Integer id) {
        return Optional.ofNullable(partnerData.get(id));
    }

    @Override
    public DeliveryPartner save(DeliveryPartner entity) {
        if (entity.getPartnerId() == null) {
            entity.setPartnerId(nextId++);
        }
        partnerData.put(entity.getPartnerId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        partnerData.remove(id);
    }
}
