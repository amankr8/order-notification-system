package com.flykraft.repository.store;

import com.flykraft.model.store.Order;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
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
    public List<Order> findAll() {
        lock.readLock().lock();
        try {
            return orderData.values().stream().map(this::clone).toList();
        } finally {
            lock.readLock().unlock();
        }
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
            if (order.getOrderId() == null) {
                order.setOrderId(nextId++);
            }
            orderData.put(order.getOrderId(), order);
            return clone(order);
        } finally {
            lock.writeLock().unlock();
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
