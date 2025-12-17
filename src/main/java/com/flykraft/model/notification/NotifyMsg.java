package com.flykraft.model.notification;

public class NotifyMsg {
    private Integer notifyMsgId;
    private Integer stakeHolderCategoryId;
    private Integer orderStatusId;
    private String message;

    public NotifyMsg(Integer stakeHolderCategoryId, Integer orderStatusId, String message) {
        this.stakeHolderCategoryId = stakeHolderCategoryId;
        this.orderStatusId = orderStatusId;
        this.message = message;
    }

    public Integer getNotifyMsgId() {
        return notifyMsgId;
    }

    public void setNotifyMsgId(Integer notifyMsgId) {
        this.notifyMsgId = notifyMsgId;
    }

    public Integer getStakeHolderCategoryId() {
        return stakeHolderCategoryId;
    }

    public void setStakeHolderCategoryId(Integer stakeHolderCategoryId) {
        this.stakeHolderCategoryId = stakeHolderCategoryId;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
