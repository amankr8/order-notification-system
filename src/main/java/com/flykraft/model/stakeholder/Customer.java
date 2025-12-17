package com.flykraft.model.stakeholder;

import java.util.Set;

public class Customer extends StakeHolder implements Notifiable {
    private Integer customerId;
    private String customerName;

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

    @Override
    public Set<Integer> getDefaultSubscriptionStatusIds() {
        return StakeHolderCategory.CUSTOMER.getDefaultSubscriptionMsgByStatusIds().keySet();
    }

    @Override
    public Set<Integer> getDefaultSubscriptionChannelIds() {
        return StakeHolderCategory.CUSTOMER.getDefaultSubscriptionChannels();
    }
}
