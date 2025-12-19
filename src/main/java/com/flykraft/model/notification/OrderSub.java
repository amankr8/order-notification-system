package com.flykraft.model.notification;

public class OrderSub {
    private Integer id;
    private Integer stakeHolderId;
    private Integer orderId;

    public OrderSub(Integer stakeHolderId, Integer orderId) {
        this.stakeHolderId = stakeHolderId;
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
