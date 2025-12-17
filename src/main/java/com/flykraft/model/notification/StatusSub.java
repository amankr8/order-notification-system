package com.flykraft.model.notification;

public class StatusSub {
    private Integer statusSubId;
    private Integer stakeHolderId;
    private Integer orderStatusId;

    public StatusSub(Integer stakeHolderId, Integer orderStatusId) {
        this.stakeHolderId = stakeHolderId;
        this.orderStatusId = orderStatusId;
    }

    public Integer getStatusSubId() {
        return statusSubId;
    }

    public void setStatusSubId(Integer statusSubId) {
        this.statusSubId = statusSubId;
    }

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }
}
