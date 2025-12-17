package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.Customer;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepo implements Repository<Integer, Customer> {
    private int nextId;
    private final Map<Integer, Customer> customerData;

    public CustomerRepo() {
        this.nextId = 1;
        this.customerData = new HashMap<>();
    }

    @Override
    public List<Customer> findAll() {
        return customerData.values().parallelStream().toList();
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return Optional.ofNullable(customerData.get(id));
    }

    @Override
    public Customer save(Customer entity) {
        if (entity.getCustomerId() == null) {
            entity.setCustomerId(nextId++);
        }
        customerData.put(entity.getCustomerId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        customerData.remove(id);
    }
}
