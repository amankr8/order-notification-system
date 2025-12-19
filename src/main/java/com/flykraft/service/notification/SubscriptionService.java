package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
import com.flykraft.model.store.OrderStatus;
import com.flykraft.repository.notification.ChannelSubRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.OrderSubRepo;
import com.flykraft.repository.notification.StatusSubRepo;
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubscriptionService {

    private final OrderSubRepo orderSubRepo;
    private final NotifyMsgRepo notifyMsgRepo;
    private final ChannelSubRepo channelSubRepo;
    private final StatusSubRepo statusSubRepo;
    private final StakeHolderService stakeHolderService;

    public SubscriptionService(OrderSubRepo orderSubRepo, NotifyMsgRepo notifyMsgRepo, ChannelSubRepo channelSubRepo, StatusSubRepo statusSubRepo, StakeHolderService stakeHolderService) {
        this.orderSubRepo = orderSubRepo;
        this.notifyMsgRepo = notifyMsgRepo;
        this.channelSubRepo = channelSubRepo;
        this.statusSubRepo = statusSubRepo;
        this.stakeHolderService = stakeHolderService;
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
        var notifySub = new OrderSub(stakeHolderId, orderId);
        orderSubRepo.save(notifySub);
    }

    public void unsubscribeFromOrder(Integer stakeHolderId, Integer orderId) {
        List<OrderSub> subs = orderSubRepo.findByOrderId(orderId);
        for (var sub : subs) {
            if (sub.getStakeHolderId().equals(stakeHolderId)) {
                orderSubRepo.deleteById(sub.getId());
            }
        }
    }

    public List<StakeHolder> getSubscribersByOrderId(Integer orderId) {
        List<OrderSub> orderSubs = orderSubRepo.findByOrderId(orderId);
        List<StakeHolder> stakeHolders = new ArrayList<>();
        for (OrderSub orderSub : orderSubs) {
            StakeHolder stakeHolder = stakeHolderService.getStakeHolderById(orderSub.getStakeHolderId());
            stakeHolders.add(stakeHolder);
        }
        return stakeHolders;
    }

    public List<OrderStatus> getStatusSubscriptionByStakeHolderId(Integer stakeHolderId) {
        List<StatusSub> statusSubs = statusSubRepo.findByStakeHolderId(stakeHolderId);
        List<OrderStatus> orderStatuses = new ArrayList<>();
        for (StatusSub statusSub : statusSubs) {
            orderStatuses.add(OrderStatus.getStatusById(statusSub.getOrderStatusId()));
        }
        return orderStatuses;
    }

    public List<Channel> getChannelSubscriptionsByStakeHolderId(Integer stakeHolderId) {
        List<ChannelSub> channelSubs = channelSubRepo.findByStakeHolderId(stakeHolderId);
        List<Channel> channels = new ArrayList<>();
        for (ChannelSub channelSub : channelSubs) {
            channels.add(Channel.getChannelById(channelSub.getChannelId()));
        }
        return channels;
    }

    public String getMessageByCategoryAndStatusId(Integer categoryId, Integer statusId) {
        List<NotifyMsg> notifyMsgs = notifyMsgRepo.findByCategoryId(categoryId);
        for (NotifyMsg notifyMsg : notifyMsgs) {
            if (notifyMsg.getOrderStatusId().equals(statusId)) {
                return notifyMsg.getMessage();
            }
        }

        throw new RuntimeException("No message found for the order status.");
    }
}
