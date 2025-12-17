package com.flykraft.model.stakeholder;

public class StakeHolder {
    private Integer stakeHolderId;
    private String stakeHolderName;
    private Integer categoryId;
    private boolean optedInForNotifications;

    public StakeHolder(String stakeHolderName, Integer categoryId) {
        this.stakeHolderName = stakeHolderName;
        this.categoryId = categoryId;
        this.optedInForNotifications = true;
    }

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public String getStakeHolderName() {
        return stakeHolderName;
    }

    public void setStakeHolderName(String stakeHolderName) {
        this.stakeHolderName = stakeHolderName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public boolean hasOptedInForNotifications() {
        return optedInForNotifications;
    }

    public void setOptedInForNotifications(boolean optedInForNotifications) {
        this.optedInForNotifications = optedInForNotifications;
    }
}
