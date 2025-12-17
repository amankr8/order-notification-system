package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.DeliveryPartner;
import com.flykraft.model.stakeholder.StakeHolder;
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
        StakeHolder stakeHolder = new StakeHolder(partner.getPartnerName(), partner.getStakeHolderCategoryId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        partner.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.subscribeToStatuses(partner.getStakeHolderId(), partner.getDefaultSubscriptionStatusIds());
        notificationService.subscribeToChannels(partner.getStakeHolderId(), partner.getDefaultSubscriptionChannelIds());
        return deliveryPartnerRepo.save(partner);
    }
}
