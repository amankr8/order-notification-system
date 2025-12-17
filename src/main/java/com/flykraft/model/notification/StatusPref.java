package com.flykraft.model.notification;

public class StatusPref {
    private Integer statusPrefId;
    private Integer stakeHolderId;
    private Integer orderStatusId;

    public StatusPref(Integer stakeHolderId, Integer orderStatusId) {
        this.stakeHolderId = stakeHolderId;
        this.orderStatusId = orderStatusId;
    }

    public Integer getStatusPrefId() {
        return statusPrefId;
    }

    public void setStatusPrefId(Integer statusPrefId) {
        this.statusPrefId = statusPrefId;
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
