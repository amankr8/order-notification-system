package com.flykraft.service.store;

import com.flykraft.model.store.Order;
import com.flykraft.model.store.OrderStatus;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.store.OrderRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.notification.SubscriptionService;
import com.flykraft.service.stakeholder.CustomerService;
import com.flykraft.service.stakeholder.DeliveryPartnerService;
import com.flykraft.service.stakeholder.VendorService;

import java.util.logging.Logger;

public class OrderService {
    private final Logger logger = Logger.getLogger(OrderService.class.getName());

    private final OrderRepo orderRepo;
    private final CustomerService customerService;
    private final VendorService vendorService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;

    public OrderService(OrderRepo orderRepo, CustomerService customerService, VendorService vendorService, DeliveryPartnerService deliveryPartnerService, SubscriptionService subscriptionService, NotificationService notificationService) {
        this.orderRepo = orderRepo;
        this.customerService = customerService;
        this.vendorService = vendorService;
        this.deliveryPartnerService = deliveryPartnerService;
        this.subscriptionService = subscriptionService;
        this.notificationService = notificationService;
    }

    public Order getOrderById(Integer orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order was not found."));
    }

    public Order createOrder(Order order) {
        try {
            Order newOrder = orderRepo.save(order);
            subscribeToRelevantStakeHolders(newOrder);
            notificationService.notify(newOrder);
            return newOrder;
        } catch (RuntimeException e) {
            logger.warning("Could not place the order: " + e.getMessage());
            throw e;
        }
    }

    public void assignDeliveryPartner(Integer orderId, Integer partnerId) {
        DeliveryPartner deliveryPartner = deliveryPartnerService.getPartnerById(partnerId);
        Order order = getOrderById(orderId);
        order.setPartnerId(deliveryPartner.getPartnerId());
        subscriptionService.subscribeToOrder(deliveryPartner.getStakeHolderId(), order.getOrderId());
        orderRepo.save(order);
    }

    private void subscribeToRelevantStakeHolders(Order order) {
        Customer customer = customerService.getCustomerById(order.getCustomerId());
        subscriptionService.subscribeToOrder(customer.getStakeHolderId(), order.getOrderId());
        Vendor vendor = vendorService.getVendorById(order.getVendorId());
        subscriptionService.subscribeToOrder(vendor.getStakeHolderId(), order.getOrderId());
    }

    public void changeStatus(Order order, OrderStatus orderStatus) {
        order.setStatusId(orderStatus.getId());
        Order updatedOrder = orderRepo.save(order);
        notificationService.notify(updatedOrder);
    }
}
