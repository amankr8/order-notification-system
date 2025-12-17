package com.flykraft;

import com.flykraft.repository.notification.ChannelPrefRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusPrefRepo;
import com.flykraft.repository.order.OrderRepo;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.repository.stakeholder.PartnerRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.order.OrderService;
import com.flykraft.service.stakeholder.StakeHolderService;

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
        PartnerRepo partnerRepo = new PartnerRepo();

        NotificationService notificationService = new NotificationService(stakeHolderRepo, notifySubRepo, notifyMsgRepo, channelPrefRepo, statusPrefRepo);

        StakeHolderService stakeHolderService = new StakeHolderService(stakeHolderRepo, customerRepo, vendorRepo, partnerRepo, notificationService);
        OrderService orderService = new OrderService(orderRepo, stakeHolderService, notificationService);

        DemoRunner.runDemo(stakeHolderService, orderService, notificationService);
    }
}