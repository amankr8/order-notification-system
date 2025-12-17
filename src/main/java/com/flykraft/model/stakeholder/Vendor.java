package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class Vendor {
    private Integer vendorId;
    private String vendorName;
    private Integer stakeHolderId;
    private final Integer stakeHolderCategoryId;
    public static final Map<Integer, String> DEFAULT_MSG_BY_STATUS = Map.of(
            OrderStatus.PLACED.getId(), "An order has been placed to your store."
    );
    public static final Set<Integer> DEFAULT_CHANNELS = Set.of(
            Channel.EMAIL.getId(),
            Channel.SMS.getId()
    );

    public Vendor(String vendorName) {
        this.vendorName = vendorName;
        this.stakeHolderCategoryId = StakeHolderCategory.VENDOR.getId();
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

    public Integer getStakeHolderId() {
        return stakeHolderId;
    }

    public void setStakeHolderId(Integer stakeHolderId) {
        this.stakeHolderId = stakeHolderId;
    }

    public Integer getStakeHolderCategoryId() {
        return stakeHolderCategoryId;
    }
}
