package com.flykraft.model.stakeholder;

public class StakeHolder {
    private Integer stakeHolderId;
    private String stakeHolderName;
    private Integer stakeHolderCategoryId;

    public StakeHolder(String stakeHolderName, Integer stakeHolderCategoryId) {
        this.stakeHolderName = stakeHolderName;
        this.stakeHolderCategoryId = stakeHolderCategoryId;
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

    public Integer getStakeHolderCategoryId() {
        return stakeHolderCategoryId;
    }

    public void setStakeHolderCategoryId(Integer stakeHolderCategoryId) {
        this.stakeHolderCategoryId = stakeHolderCategoryId;
    }
}
