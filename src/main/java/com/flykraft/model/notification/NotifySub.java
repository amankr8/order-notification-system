package com.flykraft.model.notification;

public class NotifySub {
    private Integer notifySubId;
    private Integer stakeHolderId;
    private Integer orderId;

    public NotifySub(Integer stakeHolderId, Integer orderId) {
        this.stakeHolderId = stakeHolderId;
        this.orderId = orderId;
    }

    public Integer getNotifySubId() {
        return notifySubId;
    }

    public void setNotifySubId(Integer notifySubId) {
        this.notifySubId = notifySubId;
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
