package com.flykraft.repository.order;

import com.flykraft.model.order.Order;
import com.flykraft.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderRepo implements Repository<Integer, Order> {
    private int nextId;
    private final Map<Integer, Order> orderData;

    public OrderRepo() {
        this.nextId = 1;
        this.orderData = new HashMap<>();
    }

    @Override
    public List<Order> findAll() {
        return orderData.values().parallelStream().toList();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return Optional.ofNullable(orderData.get(id));
    }

    @Override
    public Order save(Order entity) {
        if (entity.getOrderId() == null) {
            entity.setOrderId(nextId++);
        }
        orderData.put(entity.getOrderId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        orderData.remove(id);
    }
}
