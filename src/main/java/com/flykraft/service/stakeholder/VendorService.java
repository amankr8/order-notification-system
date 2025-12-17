package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
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

    public Vendor createVendor(Vendor customer) {
        StakeHolder stakeHolder = new StakeHolder(customer.getVendorName(), StakeHolderCategory.VENDOR.getId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        customer.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(customer.getStakeHolderId(), Vendor.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(customer.getStakeHolderId(), Vendor.DEFAULT_CHANNELS);
        return vendorRepo.save(customer);
    }
}
