package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class Customer {
    private Integer customerId;
    private String customerName;
    private Integer stakeHolderId;
    private final Integer stakeHolderCategoryId;

    public static final Map<Integer, String> DEFAULT_MSG_BY_STATUS = Map.of(
        OrderStatus.PLACED.getId(), "Your order has been placed successfully.",
        OrderStatus.SHIPPED.getId(), "Your order has been shipped.",
        OrderStatus.DELIVERED.getId(), "Your order has been delivered."
    );
    public static final Set<Integer> DEFAULT_CHANNELS = Set.of(
        Channel.EMAIL.getId(),
        Channel.SMS.getId()
    );

    public Customer(String customerName) {
        this.customerName = customerName;
        this.stakeHolderCategoryId = StakeHolderCategory.CUSTOMER.getId();
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(java.lang.String customerName) {
        this.customerName = customerName;
    }

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public Integer getStakeHolderCategoryId() {
        return stakeHolderCategoryId;
    }
}
