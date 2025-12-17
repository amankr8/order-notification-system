package com.flykraft;

import com.flykraft.repository.notification.ChannelPrefRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusPrefRepo;
import com.flykraft.repository.order.OrderRepo;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.repository.stakeholder.DeliveryPartnerRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.order.OrderService;
import com.flykraft.service.stakeholder.CustomerService;
import com.flykraft.service.stakeholder.DeliveryPartnerService;
import com.flykraft.service.stakeholder.StakeHolderService;
import com.flykraft.service.stakeholder.VendorService;

public class Main {
    public static void main(String[] args) {
        ChannelPrefRepo channelPrefRepo = new ChannelPrefRepo();
        NotifyMsgRepo notifyMsgRepo = new NotifyMsgRepo();
        NotifySubRepo notifySubRepo = new NotifySubRepo();
        StatusPrefRepo statusPrefRepo = new StatusPrefRepo();

        OrderRepo orderRepo = new OrderRepo();

        StakeHolderRepo stakeHolderRepo = new StakeHolderRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        VendorRepo vendorRepo = new VendorRepo();
        DeliveryPartnerRepo deliveryPartnerRepo = new DeliveryPartnerRepo();

        StakeHolderService stakeHolderService = new StakeHolderService(stakeHolderRepo);

        NotificationService notificationService = new NotificationService(notifySubRepo, notifyMsgRepo, channelPrefRepo, statusPrefRepo, stakeHolderService);

        CustomerService customerService = new CustomerService(customerRepo, stakeHolderService, notificationService);
        VendorService vendorService = new VendorService(vendorRepo, stakeHolderService, notificationService);
        DeliveryPartnerService deliveryPartnerService = new DeliveryPartnerService(deliveryPartnerRepo, stakeHolderService, notificationService);

        OrderService orderService = new OrderService(orderRepo, customerService, vendorService, notificationService);

        DemoRunner.runDemo(stakeHolderService, customerService, vendorService, deliveryPartnerService, orderService, notificationService);
    }
}