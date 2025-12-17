package com.flykraft.model.stakeholder;

import java.util.Set;

public class DeliveryPartner extends StakeHolder implements Notifiable {
    private Integer partnerId;
    private String partnerName;

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

    @Override
    public Set<Integer> getDefaultStatusIds() {
        return StakeHolderCategory.DELIVERY_PARTNER.getDefaultMsgByStatusIds().keySet();
    }

    @Override
    public Set<Integer> getDefaultChannelIds() {
        return StakeHolderCategory.DELIVERY_PARTNER.getDefaultChannels();
    }
}
