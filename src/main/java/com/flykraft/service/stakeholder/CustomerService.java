package com.flykraft.service.stakeholder;

import com.flykraft.model.notification.Channel;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.model.stakeholder.StakeHolderCategory;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.service.notification.NotificationService;

public class CustomerService {
    private final CustomerRepo customerRepo;
    private final StakeHolderService stakeHolderService;
    private final NotificationService notificationService;

    public CustomerService(CustomerRepo customerRepo, StakeHolderService stakeHolderService, NotificationService notificationService) {
        this.customerRepo = customerRepo;
        this.stakeHolderService = stakeHolderService;
        this.notificationService = notificationService;
    }

    public Customer getCustomerById(Integer id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist."));
    }

    public Customer createCustomer(Customer customer) {
        StakeHolder stakeHolder = new StakeHolder(customer.getCustomerName(), StakeHolderCategory.CUSTOMER.getId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        customer.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.addStatusPreferences(customer.getStakeHolderId(), Customer.DEFAULT_MSG_BY_STATUS.keySet());
        notificationService.addChannelPreferences(customer.getStakeHolderId(), Customer.DEFAULT_CHANNELS);
        return customerRepo.save(customer);
    }
}
