package com.flykraft.model.notification;

public class ChannelSub {
    private Integer channelSubId;
    private Integer stakeHolderId;
    private Integer channelId;

    public ChannelSub(Integer stakeHolderId, Integer channelId) {
        this.stakeHolderId = stakeHolderId;
        this.channelId = channelId;
    }

    public Integer getChannelSubId() {
        return channelSubId;
    }

    public void setChannelSubId(Integer channelSubId) {
        this.channelSubId = channelSubId;
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
