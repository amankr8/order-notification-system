package com.flykraft.model.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.order.OrderStatus;

import java.util.Map;
import java.util.Set;

public class DeliveryPartner extends StakeHolder {
    private Integer partnerId;
    private String partnerName;
    public static final Map<Integer, String> DEFAULT_MSG_BY_STATUS = Map.of(
            OrderStatus.DELIVERED.getId(), "The order has been delivered to the customer successfully."
    );
    public static final Set<Integer> DEFAULT_CHANNELS = Set.of(
            Channel.EMAIL.getId(),
            Channel.SMS.getId()
    );

    public DeliveryPartner(String partnerName) {
        super(partnerName, StakeHolderCategory.DELIVERY_PARTNER.getId());
        this.partnerName = partnerName;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
