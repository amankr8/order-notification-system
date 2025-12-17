package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class Customer extends StakeHolder {
    private Integer customerId;
    private String customerName;
    public static final Map<OrderStatus, String> DEFAULT_MSG_BY_STATUS = Map.of(
        OrderStatus.PLACED, "Your order has been placed successfully.",
        OrderStatus.SHIPPED, "Your order has been shipped.",
        OrderStatus.DELIVERED, "Your order has been delivered."
    );
    public static final Set<Channel> DEFAULT_CHANNELS = Set.of(
        Channel.EMAIL,
        Channel.SMS
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
