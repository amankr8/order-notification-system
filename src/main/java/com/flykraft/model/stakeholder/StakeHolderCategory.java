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
    private Set<Integer> defaultChannels;
    private Map<Integer, String> defaultMsgByStatusIds;

    StakeHolderCategory(Integer id, String name, Set<Integer> defaultChannels, Map<Integer, String>defaultMsgByStatusIds) {
        this.id = id;
        this.name = name;
        this.defaultChannels = defaultChannels;
        this.defaultMsgByStatusIds = defaultMsgByStatusIds;
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

    public Set<Integer> getDefaultChannels() {
        return defaultChannels;
    }

    public void setDefaultChannels(Set<Integer> defaultChannels) {
        this.defaultChannels = defaultChannels;
    }

    public Map<Integer, String> getDefaultMsgByStatusIds() {
        return defaultMsgByStatusIds;
    }

    public void setDefaultMsgByStatusIds(Map<Integer, String> defaultMsgByStatusIds) {
        this.defaultMsgByStatusIds = defaultMsgByStatusIds;
    }

    private static Set<Integer> getDefaultChannelIds() {
        return Set.of(Channel.EMAIL.getId(), Channel.SMS.getId());
    }

    private static Map<Integer, String> getDefaultMsgByStatusForCustomer() {
        return Map.of(
                OrderStatus.PLACED.getId(), "Your order has been placed successfully.",
                OrderStatus.SHIPPED.getId(), "Your order has been shipped.",
                OrderStatus.DELIVERED.getId(), "Your order has been delivered."
        );
    }

    private static Map<Integer, String> getDefaultMsgByStatusForVendor() {
        return Map.of(
                OrderStatus.PLACED.getId(), "An order has been placed to your store."
        );
    }

    private static Map<Integer, String> getDefaultMsgByStatusForPartner() {
        return Map.of(
                OrderStatus.DELIVERED.getId(), "The order has been delivered to the customer successfully."
        );
    }
}
