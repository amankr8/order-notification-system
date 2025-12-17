package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
import com.flykraft.repository.stakeholder.DeliveryPartnerRepo;
import com.flykraft.service.notification.NotificationService;

public class DeliveryPartnerService {
    private final DeliveryPartnerRepo deliveryPartnerRepo;
    private final StakeHolderService stakeHolderService;
    private final NotificationService notificationService;

    public DeliveryPartnerService(DeliveryPartnerRepo deliveryPartnerRepo, StakeHolderService stakeHolderService, NotificationService notificationService) {
        this.deliveryPartnerRepo = deliveryPartnerRepo;
        this.stakeHolderService = stakeHolderService;
        this.notificationService = notificationService;
    }

    public DeliveryPartner getPartnerById(Integer id) {
        return deliveryPartnerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public DeliveryPartner createPartner(DeliveryPartner partner) {
        StakeHolder stakeHolder = new StakeHolder(partner.getPartnerName(), StakeHolderCategory.DELIVERY_PARTNER.getId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        partner.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(partner.getStakeHolderId(), DeliveryPartner.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(partner.getStakeHolderId(), DeliveryPartner.DEFAULT_CHANNELS);
        return deliveryPartnerRepo.save(partner);
    }
}
