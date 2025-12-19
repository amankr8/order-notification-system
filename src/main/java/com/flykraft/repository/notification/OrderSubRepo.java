package com.flykraft.repository.notification;

import com.flykraft.config.GlobalConfig;
import com.flykraft.exception.DataConstraintViolationException;
import com.flykraft.model.notification.OrderSub;
import com.flykraft.repository.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderSubRepo implements Repository<Integer, OrderSub> {
    private int nextId;
    private final Map<Integer, OrderSub> orderSubData;
    private final Map<String, Integer> constraintsMap;
    private final ReadWriteLock lock;

    public OrderSubRepo() {
        this.nextId = 1;
        this.orderSubData = new HashMap<>();
        this.constraintsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<OrderSub> findById(Integer id) {
        lock.readLock().lock();
        try {
            OrderSub orderSub = orderSubData.get(id);
            return orderSub == null ? Optional.empty() : Optional.of(clone(orderSub));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public OrderSub save(OrderSub entity) {
        lock.writeLock().lock();
        try {
            OrderSub orderSub = clone(entity);
            validateConstraints(orderSub);
            if (orderSub.getId() == null) {
                orderSub.setId(nextId++);
            }
            orderSubData.put(orderSub.getId(), orderSub);
            constraintsMap.putIfAbsent(getConstraintId(orderSub), orderSub.getId());
            return clone(orderSub);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateConstraints(OrderSub orderSub) {
        String constraintId = getConstraintId(orderSub);
        if (constraintsMap.containsKey(constraintId) && !constraintsMap.get(constraintId).equals(orderSub.getId())) {
            throw new DataConstraintViolationException(GlobalConfig.DATA_CONSTRAINT_VIOLATION_MSG);
        }
    }

    @Override
    public void deleteById(Integer id) {
        lock.writeLock().lock();
        try {
            OrderSub removedEntity = orderSubData.remove(id);
            if (removedEntity != null) {
                constraintsMap.remove(getConstraintId(removedEntity));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<OrderSub> findByOrderId(Integer orderId) {
        lock.readLock().lock();
        try {
            return orderSubData.values()
                    .stream()
                    .filter(n -> n.getOrderId().equals(orderId))
                    .map(this::clone)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    private String getConstraintId(OrderSub entity) {
        if (entity.getStakeHolderId() == null || entity.getOrderId() == null) {
            throw new DataConstraintViolationException("StakeHolder Id and Order Id must not be null");
        }
        return entity.getStakeHolderId() + "&" + entity.getOrderId();
    }

    private OrderSub clone(OrderSub entity) {
        OrderSub clone = new OrderSub(entity.getStakeHolderId(), entity.getOrderId());
        clone.setId(entity.getId());
        return clone;
    }
}
