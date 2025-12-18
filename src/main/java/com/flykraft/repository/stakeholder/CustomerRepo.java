package com.flykraft.repository.stakeholder;

import com.flykraft.model.stakeholder.Customer;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomerRepo implements Repository<Integer, Customer> {
    private int nextId;
    private final Map<Integer, Customer> customerData;
    private final ReadWriteLock lock;

    public CustomerRepo() {
        this.nextId = 1;
        this.customerData = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        lock.readLock().lock();
        try {
            Customer customer = customerData.get(id);
            return customer == null ? Optional.empty() : Optional.of(clone(customer));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Customer save(Customer entity) {
        lock.writeLock().lock();
        try {
            Customer customer = clone(entity);
            if (customer.getCustomerId() == null) {
                customer.setCustomerId(nextId++);
            }
            customerData.put(customer.getCustomerId(), customer);
            return clone(customer);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            customerData.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Customer clone(Customer entity) {
        Customer clone = new Customer(entity.getCustomerName());
        clone.setCustomerId(entity.getCustomerId());
        clone.setStakeHolderId(entity.getStakeHolderId());
        clone.setStakeHolderName(entity.getStakeHolderName());
        clone.setStakeHolderCategoryId(entity.getStakeHolderCategoryId());
        clone.setOptedInForNotifications(entity.hasOptedInForNotifications());
        return clone;
    }
}
