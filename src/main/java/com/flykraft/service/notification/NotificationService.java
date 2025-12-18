package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.store.Order;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
import com.flykraft.repository.notification.ChannelSubRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusSubRepo;
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService {

    private final NotifySubRepo notifySubRepo;
    private final NotifyMsgRepo notifyMsgRepo;
    private final ChannelSubRepo channelSubRepo;
    private final StatusSubRepo statusSubRepo;
    private final StakeHolderService stakeHolderService;

    private final ExecutorService executorService;

    public NotificationService(NotifySubRepo notifySubRepo, NotifyMsgRepo notifyMsgRepo, ChannelSubRepo channelSubRepo, StatusSubRepo statusSubRepo, StakeHolderService stakeHolderService) {
        this.notifySubRepo = notifySubRepo;
        this.notifyMsgRepo = notifyMsgRepo;
        this.channelSubRepo = channelSubRepo;
        this.statusSubRepo = statusSubRepo;
        this.stakeHolderService = stakeHolderService;
        this.executorService = Executors.newFixedThreadPool(10);
        addDefaultNotificationMessages();
    }

    private void addDefaultNotificationMessages() {
        for (StakeHolderCategory stakeHolderCategory : StakeHolderCategory.values()) {
            for (Map.Entry<Integer, String> entry : stakeHolderCategory.getDefaultSubscriptionMsgByStatusIds().entrySet()) {
                Integer orderStatusId = entry.getKey();
                String message = entry.getValue();
                NotifyMsg notifyMsg = new NotifyMsg(stakeHolderCategory.getId(), orderStatusId, message);
                notifyMsgRepo.save(notifyMsg);
            }
        }
    }

    public void optInForNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(stakeHolderId);
        stakeHolder.setOptedInForNotifications(true);
        stakeHolderService.updateStakeHolder(stakeHolder);
    }

    public void optOutOfNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(stakeHolderId);
        stakeHolder.setOptedInForNotifications(false);
        stakeHolderService.updateStakeHolder(stakeHolder);
    }

    public void subscribeToStatuses(Integer stakeHolderId, Set<Integer> orderStatusIds) {
        for (Integer orderStatusId : orderStatusIds) {
            StatusSub statusSub = new StatusSub(stakeHolderId, orderStatusId);
            statusSubRepo.save(statusSub);
        }
    }

    public void unsubscribeFromStatuses(Integer stakeHolderId, Set<Integer> orderStatusIds) {
        List<StatusSub> statusSubs = statusSubRepo.findByStakeHolderId(stakeHolderId);
        for (StatusSub statusSub : statusSubs) {
            if (orderStatusIds.contains(statusSub.getOrderStatusId())) {
                statusSubRepo.deleteById(statusSub.getStatusSubId());
            }
        }
    }

    public void subscribeToChannels(Integer stakeHolderId, Set<Integer> channelIds) {
        for (Integer channelId : channelIds) {
            ChannelSub channelSub = new ChannelSub(stakeHolderId, channelId);
            channelSubRepo.save(channelSub);
        }
    }

    public void unsubscribeFromChannels(Integer stakeHolderId, Set<Integer> channelIds) {
        List<ChannelSub> channelSubs = channelSubRepo.findByStakeHolderId(stakeHolderId);
        for (ChannelSub channelSub : channelSubs) {
            if (channelIds.contains(channelSub.getChannelId())) {
                channelSubRepo.deleteById(channelSub.getChannelSubId());
            }
        }
    }

    public void updateStatusMessageForCategory(Integer categoryId, Integer statusId, String message) {
        List<NotifyMsg> msgsByCategory = notifyMsgRepo.findByCategoryId(categoryId);
        for (NotifyMsg notifyMsg : msgsByCategory) {
            if (notifyMsg.getOrderStatusId().equals(statusId)) {
                notifyMsg.setMessage(message);
                notifyMsgRepo.save(notifyMsg);
            }
        }
    }

    public void subscribeToOrder(Integer stakeHolderId, Integer orderId) {
        var notifySub = new NotifySub(stakeHolderId, orderId);
        notifySubRepo.save(notifySub);
    }

    public void unsubscribeFromOrder(Integer stakeHolderId, Integer orderId) {
        List<NotifySub> subs = notifySubRepo.findByOrderId(orderId);
        for (var sub : subs) {
            if (sub.getStakeHolderId().equals(stakeHolderId)) {
                notifySubRepo.deleteById(sub.getNotifySubId());
            }
        }
    }

    public void notify(Order order) {
        List<NotifySub> subs = notifySubRepo.findByOrderId(order.getOrderId());
        for (NotifySub sub : subs) {
            executorService.submit(() -> processNotificationForSubscriber(order, sub));
        }
    }

    private void processNotificationForSubscriber(Order order, NotifySub sub) {
        StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(sub.getStakeHolderId());
        if (stakeHolder.hasOptedInForNotifications() && validateStatusSubscriptionByStakeHolder(stakeHolder, order.getStatusId())) {
            String message = getMessageByCategoryAndStatusId(stakeHolder.getStakeHolderCategoryId(), order.getStatusId());
            List<Channel> channels = getChannelSubscriptionsByStakeHolderId(sub.getStakeHolderId());
            for (Channel channel : channels) {
                String notification = "Hi " + stakeHolder.getStakeHolderName() + "! " + message;
                executorService.submit(() -> processNotificationForChannel(channel, notification));
            }
        }
    }

    private void processNotificationForChannel(Channel channel, String notification) {
        channel.getService().sendNotification(notification);
    }

    private boolean validateStatusSubscriptionByStakeHolder(StakeHolder stakeHolder, Integer orderStatusId) {
        List<StatusSub> statusSubs = statusSubRepo.findByStakeHolderId(stakeHolder.getStakeHolderId());
        for (StatusSub statusSub : statusSubs) {
            if (statusSub.getOrderStatusId().equals(orderStatusId)) {
                return true;
            }
        }
        return false;
    }

    private List<Channel> getChannelSubscriptionsByStakeHolderId(Integer stakeHolderId) {
        List<ChannelSub> channelSubs = channelSubRepo.findByStakeHolderId(stakeHolderId);
        List<Channel> channels = new ArrayList<>();
        for (ChannelSub channelSub : channelSubs) {
            channels.add(Channel.getChannelById(channelSub.getChannelId()));
        }
        return channels;
    }

    private String getMessageByCategoryAndStatusId(Integer categoryId, Integer statusId) {
        List<NotifyMsg> notifyMsgs = notifyMsgRepo.findByCategoryId(categoryId);
        for (NotifyMsg notifyMsg : notifyMsgs) {
            if (notifyMsg.getOrderStatusId().equals(statusId)) {
                return notifyMsg.getMessage();
            }
        }

        throw new RuntimeException("No message found for the order status.");
    }
}
