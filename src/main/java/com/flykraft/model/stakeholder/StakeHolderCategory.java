package com.flykraft.model.stakeholder;

import com.flykraft.model.order.OrderStatus;

import java.util.Map;

public enum StakeHolderCategory {
    CUSTOMER(1, "Customer", Customer.DEFAULT_MSG_BY_STATUS),
    VENDOR(2, "Vendor", Vendor.DEFAULT_MSG_BY_STATUS),
    DELIVERY_PARTNER(3, "Delivery Partner", DeliveryPartner.DEFAULT_MSG_BY_STATUS);

    private Integer id;
    private String name;
    private Map<OrderStatus, String> defaultMsgByStatus;

    StakeHolderCategory(Integer id, String name, Map<OrderStatus, String> defaultMsgByStatus) {
        this.id = id;
        this.name = name;
        this.defaultMsgByStatus = defaultMsgByStatus;
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

    public Map<OrderStatus, String> getDefaultMsgByStatus() {
        return defaultMsgByStatus;
    }

    public void setDefaultMsgByStatus(Map<OrderStatus, String> defaultMsgByStatus) {
        this.defaultMsgByStatus = defaultMsgByStatus;
    }
}
