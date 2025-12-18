package com.flykraft.repository.store;

import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.store.Order;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderRepo implements Repository<Integer, Order> {
    private int nextId;
    private final Map<Integer, Order> orderData;
    private final ReadWriteLock lock;

    public OrderRepo() {
        this.nextId = 1;
        this.orderData = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        lock.readLock().lock();
        try {
            Order order = orderData.get(id);
            return order == null ? Optional.empty() : Optional.of(clone(order));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Order save(Order entity) {
        lock.writeLock().lock();
        try {
            Order order = clone(entity);
            validateConstraints(order);
            if (order.getOrderId() == null) {
                order.setOrderId(nextId++);
            }
            orderData.put(order.getOrderId(), order);
            return clone(order);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraints(Order order) {
        if (order.getCustomerId() == null || order.getVendorId() == null || order.getStatusId() == null) {
            throw new DataConstraintViolationException("Customer Id, Vendor Id, and Status Id cannot be null");
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            orderData.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Order clone(Order entity) {
        Order clone = new Order(entity.getCustomerId(), entity.getVendorId());
        clone.setOrderId(entity.getOrderId());
        clone.setPartnerId(entity.getPartnerId());
        clone.setStatusId(entity.getStatusId());
        return clone;
    }
}
