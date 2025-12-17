package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class Customer extends StakeHolder {
    private Integer customerId;
    private String customerName;
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
        super(customerName, StakeHolderCategory.CUSTOMER.getId());
        this.customerName = customerName;
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
}
