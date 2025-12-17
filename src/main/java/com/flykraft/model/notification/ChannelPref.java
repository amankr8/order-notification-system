package com.flykraft.model.notification;

public class ChannelPref {
    private Integer channelPrefId;
    private Integer stakeHolderId;
    private Integer channelId;

    public ChannelPref(Integer stakeHolderId, Integer channelId) {
        this.stakeHolderId = stakeHolderId;
        this.channelId = channelId;
    }

    public Integer getChannelPrefId() {
        return channelPrefId;
    }

    public void setChannelPrefId(Integer channelPrefId) {
        this.channelPrefId = channelPrefId;
    }

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}
