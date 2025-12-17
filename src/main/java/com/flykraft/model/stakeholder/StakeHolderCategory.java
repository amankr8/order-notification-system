package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public enum StakeHolderCategory {
    CUSTOMER(1, "Customer", getDefaultChannelIds(), getDefaultMsgByStatusForCustomer()),
    VENDOR(2, "Vendor", getDefaultChannelIds(), getDefaultMsgByStatusForVendor()),
    DELIVERY_PARTNER(3, "Delivery Partner", getDefaultChannelIds(), getDefaultMsgByStatusForPartner());

    private Integer id;
    private String name;
    private Set<Integer> defaultSubscriptionChannels;
    private Map<Integer, String> defaultSubscriptionMsgByStatusIds;

    StakeHolderCategory(Integer id, String name, Set<Integer> defaultSubscriptionChannels, Map<Integer, String> defaultSubscriptionMsgByStatusIds) {
        this.id = id;
        this.name = name;
        this.defaultSubscriptionChannels = defaultSubscriptionChannels;
        this.defaultSubscriptionMsgByStatusIds = defaultSubscriptionMsgByStatusIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getDefaultSubscriptionChannels() {
        return defaultSubscriptionChannels;
    }

    public void setDefaultSubscriptionChannels(Set<Integer> defaultSubscriptionChannels) {
        this.defaultSubscriptionChannels = defaultSubscriptionChannels;
    }

    public Map<Integer, String> getDefaultSubscriptionMsgByStatusIds() {
        return defaultSubscriptionMsgByStatusIds;
    }

    public void setDefaultSubscriptionMsgByStatusIds(Map<Integer, String> defaultSubscriptionMsgByStatusIds) {
        this.defaultSubscriptionMsgByStatusIds = defaultSubscriptionMsgByStatusIds;
    }

    private static Set<Integer> getDefaultChannelIds() {
        return Set.of(Channel.EMAIL.getId(), Channel.SMS.getId());
    }

    private static Map<Integer, String> getDefaultMsgByStatusForCustomer() {
        return Map.of(
            OrderStatus.PLACED.getId(), "Your order was placed successfully.",
            OrderStatus.SHIPPED.getId(), "Your order was shipped.",
            OrderStatus.DELIVERED.getId(), "Your order was delivered."
        );
    }

    private static Map<Integer, String> getDefaultMsgByStatusForVendor() {
        return Map.of(
            OrderStatus.PLACED.getId(), "An order was placed to your store."
        );
    }

    private static Map<Integer, String> getDefaultMsgByStatusForPartner() {
        return Map.of(
            OrderStatus.DELIVERED.getId(), "The order was delivered to the customer successfully."
        );
    }
}
