package com.flykraft;

import com.flykraft.repository.notification.ChannelSubRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.OrderSubRepo;
import com.flykraft.repository.notification.StatusSubRepo;
import com.flykraft.repository.store.OrderRepo;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.repository.stakeholder.DeliveryPartnerRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;
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

        NotificationService notificationService = new NotificationService(orderSubRepo, notifyMsgRepo, channelSubRepo, statusSubRepo, stakeHolderService);

        CustomerService customerService = new CustomerService(customerRepo, stakeHolderService, notificationService);
        VendorService vendorService = new VendorService(vendorRepo, stakeHolderService, notificationService);
        DeliveryPartnerService deliveryPartnerService = new DeliveryPartnerService(deliveryPartnerRepo, stakeHolderService, notificationService);

        OrderService orderService = new OrderService(orderRepo, customerService, vendorService, deliveryPartnerService, notificationService);

        DemoRunner.runDemo(stakeHolderService, customerService, vendorService, deliveryPartnerService, orderService, notificationService);
    }
}