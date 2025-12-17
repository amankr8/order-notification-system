package com.flykraft.service.order;

import com.flykraft.model.order.Order;
import com.flykraft.model.order.OrderStatus;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.order.OrderRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.stakeholder.StakeHolderService;

public class OrderService {

    private final OrderRepo orderRepo;
    private final StakeHolderService stakeHolderService;
    private final NotificationService notificationService;

    public OrderService(OrderRepo orderRepo, StakeHolderService stakeHolderService, NotificationService notificationService) {
        this.orderRepo = orderRepo;
        this.stakeHolderService = stakeHolderService;
        this.notificationService = notificationService;
    }

    public Order getOrderById(Integer orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order was not found."));
    }

    public Order createOrder(Order order) {
        Order newOrder = orderRepo.save(order);
        subscribeToRelevantStakeHolders(order);
        notificationService.notify(order);
        return newOrder;
    }

    public void assignDeliveryPartner(Integer orderId, DeliveryPartner deliveryPartner) {
        Order order = getOrderById(orderId);
        order.setPartnerId(deliveryPartner.getPartnerId());
        notificationService.subscribe(deliveryPartner.getStakeHolderId(), order.getOrderId());
        orderRepo.save(order);
    }

    private void subscribeToRelevantStakeHolders(Order order) {
        Customer customer = stakeHolderService.getCustomerById(order.getCustomerId());
        notificationService.subscribe(customer.getStakeHolderId(), order.getOrderId());
        Vendor vendor = stakeHolderService.getVendorById(order.getVendorId());
        notificationService.subscribe(vendor.getStakeHolderId(), order.getOrderId());
    }

    public void changeStatus(Order order, OrderStatus orderStatus) {
        order.setStatusId(orderStatus.getId());
        Order updatedOrder = orderRepo.save(order);
        notificationService.notify(updatedOrder);
    }
}
