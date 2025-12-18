package com.flykraft.model.notification;

public class CommDetail {
    private Integer id;
    private Integer stakeHolderId;
    private String email;
    private String phNumber;

    public CommDetail(Integer stakeHolderId, String email, String phNumber) {
        this.stakeHolderId = stakeHolderId;
        this.email = email;
        this.phNumber = phNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }
}
