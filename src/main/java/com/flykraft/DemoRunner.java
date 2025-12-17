package com.flykraft;

import com.flykraft.model.order.Order;
import com.flykraft.model.order.OrderStatus;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.order.OrderService;
import com.flykraft.service.stakeholder.CustomerService;
import com.flykraft.service.stakeholder.DeliveryPartnerService;
import com.flykraft.service.stakeholder.StakeHolderService;
import com.flykraft.service.stakeholder.VendorService;

public class DemoRunner {
    public static void runDemo(StakeHolderService stakeHolderService, CustomerService customerService, VendorService vendorService, DeliveryPartnerService deliveryPartnerService, OrderService orderService, NotificationService notificationService) {
        Customer customer1 = customerService.createCustomer(new Customer("John Doe"));
        Customer customer2 = customerService.createCustomer(new Customer("Michael Horn"));
        Vendor vendor = vendorService.createVendor(new Vendor("Best Vendor"));
        DeliveryPartner deliveryPartner = deliveryPartnerService.createPartner(new DeliveryPartner("Ekart Logistics"));

        System.out.println("\nCreating an order...");
        Order order1 = orderService.createOrder(new Order(customer1.getCustomerId(), vendor.getVendorId()));
        Order order2 = orderService.createOrder(new Order(customer2.getCustomerId(), vendor.getVendorId()));

        notificationService.optOutOfNotifications(customer1.getStakeHolderId());
        System.out.println("\n" + customer1.getCustomerName() + " has opted out of notifications.");

        orderService.assignDeliveryPartner(order1.getOrderId(), deliveryPartner);
        System.out.println("\nDelivery Partner assigned -> " + deliveryPartner.getPartnerName());

        System.out.println("\nShipping the order...");
        orderService.changeStatus(order1, OrderStatus.SHIPPED);

        notificationService.optInForNotifications(customer1.getStakeHolderId());
        System.out.println("\n" + customer1.getCustomerName() + " has opted in for notifications.");

        System.out.println("\nDelivering the order...");
        orderService.changeStatus(order1, OrderStatus.DELIVERED);
    }
}
