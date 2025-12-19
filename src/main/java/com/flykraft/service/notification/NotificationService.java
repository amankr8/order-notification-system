package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.store.Order;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.store.OrderStatus;
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class NotificationService {
    public static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final SubscriptionService subscriptionService;
    private final StakeHolderService stakeHolderService;
    private final ExecutorService executorService;

    public NotificationService(SubscriptionService subscriptionService, StakeHolderService stakeHolderService) {
        this.subscriptionService = subscriptionService;
        this.stakeHolderService = stakeHolderService;
        this.executorService = Executors.newFixedThreadPool(10, task -> {
            Thread t = new Thread(task);
            t.setDaemon(true);
            return t;
        });
    }

    public void notify(Order order) {
        List<StakeHolder> subscribers = subscriptionService.getSubscribersByOrderId(order.getOrderId());
        for (StakeHolder subscriber : subscribers) {
            if (validateStatusSubscriptionByStakeHolder(subscriber.getStakeHolderId(), order.getStatusId())) {
                executorService.submit(() -> {
                    try {
                        processNotificationForSubscriber(subscriber, order);
                    } catch (Exception e) {
                        logger.info("Failed to process notification for Stakeholder ID: " + subscriber.getStakeHolderId() + " and Order ID: " + order.getOrderId() + " - " + e.getMessage());
                    }
                });
            }
        }
    }

    public void replayNotificationToStakeholder(Order order, OrderStatus orderStatus, Integer stakeHolderId) {
        if (order.getStatusId() < orderStatus.getId()) {
            throw new IllegalArgumentException("Invalid replay of order status!");
        }
        StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(stakeHolderId);
        if (validateStatusSubscriptionByStakeHolder(stakeHolder.getStakeHolderId(), order.getStatusId())) {
            processNotificationForSubscriber(stakeHolder, order, orderStatus.getId());
        }
    }

    private void processNotificationForSubscriber(StakeHolder stakeHolder, Order order) {
        processNotificationForSubscriber(stakeHolder, order, order.getStatusId());
    }

    private void processNotificationForSubscriber(StakeHolder stakeHolder, Order order, Integer statusId) {
        String message = subscriptionService.getMessageByCategoryAndStatusId(stakeHolder.getStakeHolderCategoryId(), statusId);
        String notification = "[ORDER ID:" + order.getOrderId() + "][CUSTOMER ID:" + order.getCustomerId() + "][VENDOR ID:" + order.getVendorId() + "] - " + message;
        List<Channel> channels = subscriptionService.getChannelSubscriptionsByStakeHolderId(stakeHolder.getStakeHolderId());
        for (Channel channel : channels) {
            executorService.submit(() -> processNotificationForChannel(channel, notification));
        }
    }

    private void processNotificationForChannel(Channel channel, String notification) {
        channel.getService().sendNotification(notification);
    }

    public boolean validateStatusSubscriptionByStakeHolder(Integer stakeHolderId, Integer orderStatusId) {
        List<OrderStatus> orderStatuses = subscriptionService.getStatusSubscriptionByStakeHolderId(stakeHolderId);
        for (OrderStatus orderStatus : orderStatuses) {
            if (orderStatus.getId().equals(orderStatusId)) {
                return true;
            }
        }
        return false;
    }
}
