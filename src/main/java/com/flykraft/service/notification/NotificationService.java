package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.order.Order;
import com.flykraft.model.order.OrderStatus;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
import com.flykraft.repository.notification.ChannelPrefRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusPrefRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationService {

    private final StakeHolderRepo stakeHolderRepo;
    private final NotifySubRepo notifySubRepo;
    private final NotifyMsgRepo notifyMsgRepo;
    private final ChannelPrefRepo channelPrefRepo;
    private final StatusPrefRepo statusPrefRepo;

    public NotificationService(StakeHolderRepo stakeHolderRepo, NotifySubRepo notifySubRepo, NotifyMsgRepo notifyMsgRepo, ChannelPrefRepo channelPrefRepo, StatusPrefRepo statusPrefRepo) {
        this.stakeHolderRepo = stakeHolderRepo;
        this.notifySubRepo = notifySubRepo;
        this.notifyMsgRepo = notifyMsgRepo;
        this.channelPrefRepo = channelPrefRepo;
        this.statusPrefRepo = statusPrefRepo;
        addDefaultNotificationMessages();
    }

    private void addDefaultNotificationMessages() {
        for (StakeHolderCategory stakeHolderCategory : StakeHolderCategory.values()) {
            for (Map.Entry<OrderStatus, String> entry : stakeHolderCategory.getDefaultMsgByStatus().entrySet()) {
                OrderStatus orderStatus = entry.getKey();
                String message = entry.getValue();
                NotifyMsg notifyMsg = new NotifyMsg(stakeHolderCategory.getId(), orderStatus.getId(), message);
                notifyMsgRepo.save(notifyMsg);
            }
        }
    }

    public void optInForNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = stakeHolderRepo.findById(stakeHolderId).orElseThrow();
        stakeHolder.setOptedInForNotifications(true);
        stakeHolderRepo.save(stakeHolder);
    }

    public void optOutOfNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = stakeHolderRepo.findById(stakeHolderId).orElseThrow();
        stakeHolder.setOptedInForNotifications(false);
        stakeHolderRepo.save(stakeHolder);
    }

    public void addStatusPreferences(Integer stakeHolderId, Set<OrderStatus> orderStatuses) {
        for (OrderStatus orderStatus : orderStatuses) {
            StatusPref statusPref = new StatusPref(stakeHolderId, orderStatus.getId());
            statusPrefRepo.save(statusPref);
        }
    }

    public void addChannelPreferences(Integer stakeHolderId, Set<Channel> channels) {
        for (Channel channel : channels) {
            ChannelPref channelPref = new ChannelPref(stakeHolderId, channel.getId());
            channelPrefRepo.save(channelPref);
        }
    }

    public void subscribe(Integer stakeHolderId, Integer orderId) {
        var notifySub = new NotifySub(stakeHolderId, orderId);
        notifySubRepo.save(notifySub);
    }

    public void unsubscribe(Integer stakeHolderId, Integer orderId) {
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
            StakeHolder stakeHolder = stakeHolderRepo.findById(sub.getStakeHolderId()).orElseThrow();
            if (stakeHolder.isOptedInForNotifications() && validatePreferenceByStakeHolderId(stakeHolder, order.getStatusId())) {
                String message = getMessageByStakeHolderAndStatusId(stakeHolder, order.getStatusId());
                List<Channel> channels = getPreferredChannelsByStakeHolderId(sub.getStakeHolderId());
                for (Channel channel : channels) {
                    channel.getService().sendNotification(message);
                }
            }
        }
    }

    private boolean validatePreferenceByStakeHolderId(StakeHolder stakeHolder, Integer orderStatusId) {
        List<StatusPref> statusPrefs = statusPrefRepo.findByStakeHolderId(stakeHolder.getStakeHolderId());
        for (StatusPref statusPref : statusPrefs) {
            if (statusPref.getOrderStatusId().equals(orderStatusId)) {
                return true;
            }
        }
        return false;
    }

    private List<Channel> getPreferredChannelsByStakeHolderId(Integer stakeHolderId) {
        List<ChannelPref> channelPrefs = channelPrefRepo.findByStakeHolderId(stakeHolderId);
        List<Channel> channels = new ArrayList<>();
        for (ChannelPref channelPref : channelPrefs) {
            channels.add(Channel.getChannelById(channelPref.getChannelId()));
        }
        return channels;
    }

    private String getMessageByStakeHolderAndStatusId(StakeHolder stakeHolder, Integer statusId) {
        List<NotifyMsg> notifyMsgs = notifyMsgRepo.findByCategoryId(stakeHolder.getCategoryId());
        for (NotifyMsg notifyMsg : notifyMsgs) {
            if (notifyMsg.getOrderStatusId().equals(statusId)) {
                return "Hi " + stakeHolder.getStakeHolderName() + "! " + notifyMsg.getMessage();
            }
        }

        throw new RuntimeException("No message found for the order status.");
    }
}
