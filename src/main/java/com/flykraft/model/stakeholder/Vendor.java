package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class Vendor extends StakeHolder {
    private Integer vendorId;
    private String vendorName;
    public static final Map<Integer, String> DEFAULT_MSG_BY_STATUS = Map.of(
            OrderStatus.PLACED.getId(), "An order has been placed to your store."
    );
    public static final Set<Integer> DEFAULT_CHANNELS = Set.of(
            Channel.EMAIL.getId(),
            Channel.SMS.getId()
    );

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
}
