package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;

public class VendorService {
    private final VendorRepo vendorRepo;
    private final StakeHolderService stakeHolderService;
    private final NotificationService notificationService;

    public VendorService(VendorRepo vendorRepo, StakeHolderService stakeHolderService, NotificationService notificationService) {
        this.vendorRepo = vendorRepo;
        this.stakeHolderService = stakeHolderService;
        this.notificationService = notificationService;
    }

    public Vendor getVendorById(Integer id) {
        return vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public Vendor createVendor(Vendor vendor) {
        StakeHolder stakeHolder = new StakeHolder(vendor.getVendorName(), vendor.getStakeHolderCategoryId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        vendor.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.subscribeToStatuses(vendor.getStakeHolderId(), vendor.getDefaultSubscriptionStatusIds());
        notificationService.subscribeToChannels(vendor.getStakeHolderId(), vendor.getDefaultSubscriptionChannelIds());
        return vendorRepo.save(vendor);
    }
}
