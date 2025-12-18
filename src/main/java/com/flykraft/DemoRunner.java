package com.flykraft;

import com.flykraft.model.store.Order;
import com.flykraft.model.store.OrderStatus;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.store.OrderService;
import com.flykraft.service.stakeholder.CustomerService;
import com.flykraft.service.stakeholder.DeliveryPartnerService;
import com.flykraft.service.stakeholder.StakeHolderService;
import com.flykraft.service.stakeholder.VendorService;

import java.util.Set;

public class DemoRunner {
    public static void runDemo(StakeHolderService stakeHolderService, CustomerService customerService, VendorService vendorService, DeliveryPartnerService deliveryPartnerService, OrderService orderService, NotificationService notificationService) {
        try {
            Customer customer1 = customerService.createCustomer(new Customer("John Doe"));
            Customer customer2 = customerService.createCustomer(new Customer("Michael Horn"));
            Vendor vendor = vendorService.createVendor(new Vendor("Best Vendor"));
            DeliveryPartner deliveryPartner = deliveryPartnerService.createPartner(new DeliveryPartner("Ekart Logistics"));

            notificationService.unsubscribeFromStatuses(customer1.getStakeHolderId(), Set.of(OrderStatus.DELIVERED.getId()));
            System.out.println("\n" + customer1.getCustomerName() + " has removed " + OrderStatus.DELIVERED.getName() + " status from his update preferences");

            System.out.println("\nCreating orders...");
            Order order1 = orderService.createOrder(new Order(customer1.getCustomerId(), vendor.getVendorId()));
            Order order2 = orderService.createOrder(new Order(customer2.getCustomerId(), vendor.getVendorId()));

            Thread.sleep(2000);

            notificationService.subscribeToStatuses(customer1.getStakeHolderId(), Set.of(OrderStatus.DELIVERED.getId()));
            System.out.println("\n" + customer1.getCustomerName() + " has added " + OrderStatus.DELIVERED.getName() + " status to his update preferences");

            orderService.assignDeliveryPartner(order1.getOrderId(), deliveryPartner.getPartnerId());
            System.out.println("\nDelivery Partner assigned -> " + deliveryPartner.getPartnerName());

            System.out.println("\nShipping orders...");
            orderService.changeStatus(order1, OrderStatus.SHIPPED);

            Thread.sleep(2000);

            notificationService.unsubscribeFromOrder(customer1.getStakeHolderId(), order1.getOrderId());
            System.out.println("\n" + customer1.getCustomerName() + " has unsubscribed from order ID: " + order1.getOrderId());

            System.out.println("\nDelivering orders...");
            orderService.changeStatus(order1, OrderStatus.DELIVERED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
