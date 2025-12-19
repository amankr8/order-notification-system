package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.Vendor;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.SubscriptionService;

public class VendorService {
    private final VendorRepo vendorRepo;
    private final StakeHolderService stakeHolderService;
    private final SubscriptionService subscriptionService;

    public VendorService(VendorRepo vendorRepo, StakeHolderService stakeHolderService, SubscriptionService subscriptionService) {
        this.vendorRepo = vendorRepo;
        this.stakeHolderService = stakeHolderService;
        this.subscriptionService = subscriptionService;
    }

    public Vendor getVendorById(Integer id) {
        return vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public Vendor createVendor(Vendor vendor) {
        StakeHolder stakeHolder = new StakeHolder(vendor.getVendorName(), vendor.getStakeHolderCategoryId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        vendor.setStakeHolderId(stakeHolder.getStakeHolderId());
        subscriptionService.subscribeToStatuses(vendor.getStakeHolderId(), vendor.getDefaultSubscriptionStatusIds());
        subscriptionService.subscribeToChannels(vendor.getStakeHolderId(), vendor.getDefaultSubscriptionChannelIds());
        return vendorRepo.save(vendor);
    }
}
