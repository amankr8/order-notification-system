package com.flykraft;

import com.flykraft.repository.notification.*;
import com.flykraft.repository.store.OrderRepo;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.repository.stakeholder.DeliveryPartnerRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.notification.SubscriptionService;
import com.flykraft.service.store.OrderService;
import com.flykraft.service.stakeholder.CustomerService;
import com.flykraft.service.stakeholder.DeliveryPartnerService;
import com.flykraft.service.stakeholder.StakeHolderService;
import com.flykraft.service.stakeholder.VendorService;

public class Main {
    public static void main(String[] args) {
        ChannelSubRepo channelSubRepo = new ChannelSubRepo();
        NotifyMsgRepo notifyMsgRepo = new NotifyMsgRepo();
        OrderSubRepo orderSubRepo = new OrderSubRepo();
        StatusSubRepo statusSubRepo = new StatusSubRepo();

        OrderRepo orderRepo = new OrderRepo();

        StakeHolderRepo stakeHolderRepo = new StakeHolderRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        VendorRepo vendorRepo = new VendorRepo();
        DeliveryPartnerRepo deliveryPartnerRepo = new DeliveryPartnerRepo();

        StakeHolderService stakeHolderService = new StakeHolderService(stakeHolderRepo);

        SubscriptionService subscriptionService = new SubscriptionService(orderSubRepo, notifyMsgRepo, channelSubRepo, statusSubRepo, stakeHolderService);
        NotificationService notificationService = new NotificationService(subscriptionService, stakeHolderService);

        CustomerService customerService = new CustomerService(customerRepo, stakeHolderService, subscriptionService);
        VendorService vendorService = new VendorService(vendorRepo, stakeHolderService, subscriptionService);
        DeliveryPartnerService deliveryPartnerService = new DeliveryPartnerService(deliveryPartnerRepo, stakeHolderService, subscriptionService);

        OrderService orderService = new OrderService(orderRepo, customerService, vendorService, deliveryPartnerService, subscriptionService, notificationService);

        DemoRunner.runDemo(customerService, vendorService, deliveryPartnerService, orderService, subscriptionService);
    }
}