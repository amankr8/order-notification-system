package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.*;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.repository.stakeholder.PartnerRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.repository.stakeholder.VendorRepo;
import com.flykraft.service.notification.NotificationService;

public class StakeHolderService {

    private final StakeHolderRepo stakeHolderRepo;
    private final CustomerRepo customerRepo;
    private final VendorRepo vendorRepo;
    private final PartnerRepo partnerRepo;
    private final NotificationService notificationService;

    public StakeHolderService(StakeHolderRepo stakeHolderRepo, CustomerRepo customerRepo, VendorRepo vendorRepo, PartnerRepo partnerRepo, NotificationService notificationService) {
        this.stakeHolderRepo = stakeHolderRepo;
        this.customerRepo = customerRepo;
        this.vendorRepo = vendorRepo;
        this.partnerRepo = partnerRepo;
        this.notificationService = notificationService;
    }

    public Customer getCustomerById(Integer id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public Vendor getVendorById(Integer id) {
        return vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public Customer addCustomer(Customer customer) {
        StakeHolder stakeHolder = new StakeHolder(customer.getCustomerName(), StakeHolderCategory.CUSTOMER.getId());
        stakeHolder = stakeHolderRepo.save(stakeHolder);
        customer.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(customer.getStakeHolderId(), Customer.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(customer.getStakeHolderId(), Customer.DEFAULT_CHANNELS);
        return customerRepo.save(customer);
    }

    public Vendor addVendor(Vendor vendor) {
        StakeHolder stakeHolder = new StakeHolder(vendor.getVendorName(), StakeHolderCategory.VENDOR.getId());
        stakeHolder = stakeHolderRepo.save(stakeHolder);
        vendor.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(vendor.getStakeHolderId(), Vendor.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(vendor.getStakeHolderId(), Vendor.DEFAULT_CHANNELS);
        return vendorRepo.save(vendor);
    }

    public DeliveryPartner addPartner(DeliveryPartner deliveryPartner) {
        StakeHolder stakeHolder = new StakeHolder(deliveryPartner.getPartnerName(), StakeHolderCategory.DELIVERY_PARTNER.getId());
        stakeHolder = stakeHolderRepo.save(stakeHolder);
        deliveryPartner.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(deliveryPartner.getStakeHolderId(), DeliveryPartner.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(deliveryPartner.getStakeHolderId(), DeliveryPartner.DEFAULT_CHANNELS);
        return partnerRepo.save(deliveryPartner);
    }
}
