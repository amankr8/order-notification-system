package com.flykraft.model.stakeholder;

import java.util.Set;

public class Vendor extends StakeHolder implements Notifiable {
    private Integer vendorId;
    private String vendorName;

    public Vendor(String vendorName) {
        super(vendorName, StakeHolderCategory.VENDOR.getId());
        this.vendorName = vendorName;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    @Override
    public Set<Integer> getDefaultStatusIds() {
        return StakeHolderCategory.VENDOR.getDefaultMsgByStatusIds().keySet();
    }

    @Override
    public Set<Integer> getDefaultChannelIds() {
        return StakeHolderCategory.VENDOR.getDefaultChannels();
    }
}
