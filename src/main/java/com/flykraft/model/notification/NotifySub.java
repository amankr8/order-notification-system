package com.flykraft.model.notification;

public class NotifySub {
    private Integer id;
    private Integer stakeHolderId;
    private Boolean optedInForNotifications;

    public NotifySub(Integer stakeHolderId, Boolean optedInForNotifications) {
        this.stakeHolderId = stakeHolderId;
        this.optedInForNotifications = optedInForNotifications;
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

    public Boolean getOptedInForNotifications() {
        return optedInForNotifications;
    }

    public void setOptedInForNotifications(Boolean optedInForNotifications) {
        this.optedInForNotifications = optedInForNotifications;
    }
}
