package com.flykraft.service.stakeholder;

import com.flykraft.exception.ResourceNotFoundException;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.StakeHolder;
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
                .orElseThrow(() -> new ResourceNotFoundException("Customer does not exist for the ID: " + id));
    }

    public Customer createCustomer(Customer customer) {
        StakeHolder stakeHolder = new StakeHolder(customer.getCustomerName(), customer.getStakeHolderCategoryId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        customer.setStakeHolderId(stakeHolder.getStakeHolderId());
        notificationService.subscribeToStatuses(customer.getStakeHolderId(), customer.getDefaultSubscriptionStatusIds());
        notificationService.subscribeToChannels(customer.getStakeHolderId(), customer.getDefaultSubscriptionChannelIds());
        return customerRepo.save(customer);
    }
}
