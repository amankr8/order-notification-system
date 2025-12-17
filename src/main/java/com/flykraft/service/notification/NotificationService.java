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
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationService {

    private final NotifySubRepo notifySubRepo;
    private final NotifyMsgRepo notifyMsgRepo;
    private final ChannelPrefRepo channelPrefRepo;
    private final StatusPrefRepo statusPrefRepo;
    private final StakeHolderService stakeHolderService;

    public NotificationService(NotifySubRepo notifySubRepo, NotifyMsgRepo notifyMsgRepo, ChannelPrefRepo channelPrefRepo, StatusPrefRepo statusPrefRepo, StakeHolderService stakeHolderService) {
        this.notifySubRepo = notifySubRepo;
        this.notifyMsgRepo = notifyMsgRepo;
        this.channelPrefRepo = channelPrefRepo;
        this.statusPrefRepo = statusPrefRepo;
        this.stakeHolderService = stakeHolderService;
        addDefaultNotificationMessages();
    }

    private void addDefaultNotificationMessages() {
        for (StakeHolderCategory stakeHolderCategory : StakeHolderCategory.values()) {
            for (Map.Entry<Integer, String> entry : stakeHolderCategory.getDefaultMsgByStatus().entrySet()) {
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

    public void addStatusPreferences(Integer stakeHolderId, Set<Integer> orderStatusIds) {
        for (Integer orderStatusId : orderStatusIds) {
            StatusPref statusPref = new StatusPref(stakeHolderId, orderStatusId);
            statusPrefRepo.save(statusPref);
        }
    }

    public void removeStatusPreferences(Integer stakeHolderId, Set<Integer> orderStatusIds) {
        List<StatusPref> statusPrefs = statusPrefRepo.findByStakeHolderId(stakeHolderId);
        for (StatusPref statusPref : statusPrefs) {
            if (orderStatusIds.contains(statusPref.getOrderStatusId())) {
                statusPrefRepo.deleteById(statusPref.getStatusPrefId());
            }
        }
    }

    public void addChannelPreferences(Integer stakeHolderId, Set<Integer> channelIds) {
        for (Integer channelId : channelIds) {
            ChannelPref channelPref = new ChannelPref(stakeHolderId, channelId);
            channelPrefRepo.save(channelPref);
        }
    }

    public void removeChannelPreferences(Integer stakeHolderId, Set<Integer> channelIds) {
        List<ChannelPref> channelPrefs = channelPrefRepo.findByStakeHolderId(stakeHolderId);
        for (ChannelPref channelPref : channelPrefs) {
            if (channelIds.contains(channelPref.getChannelId())) {
                channelPrefRepo.deleteById(channelPref.getChannelPrefId());
            }
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
            StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(sub.getStakeHolderId());
            if (stakeHolder.hasOptedInForNotifications() && validatePreferenceByStakeHolder(stakeHolder, order.getStatusId())) {
                String message = getMessageByStakeHolderAndStatusId(stakeHolder, order.getStatusId());
                List<Channel> channels = getPreferredChannelsByStakeHolderId(sub.getStakeHolderId());
                for (Channel channel : channels) {
                    channel.getService().sendNotification(message);
                }
            }
        }
    }

    private boolean validatePreferenceByStakeHolder(StakeHolder stakeHolder, Integer orderStatusId) {
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
