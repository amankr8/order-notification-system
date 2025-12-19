package com.flykraft.service.stakeholder;

import com.flykraft.exception.ResourceNotFoundException;
import com.flykraft.model.stakeholder.Customer;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.repository.stakeholder.CustomerRepo;
import com.flykraft.service.notification.NotificationService;
import com.flykraft.service.notification.SubscriptionService;

public class CustomerService {
    private final CustomerRepo customerRepo;
    private final StakeHolderService stakeHolderService;
    private final SubscriptionService subscriptionService;

    public CustomerService(CustomerRepo customerRepo, StakeHolderService stakeHolderService, SubscriptionService subscriptionService) {
        this.customerRepo = customerRepo;
        this.stakeHolderService = stakeHolderService;
        this.subscriptionService = subscriptionService;
    }

    public Customer getCustomerById(Integer id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer does not exist for the ID: " + id));
    }

    public Customer createCustomer(Customer customer) {
        StakeHolder stakeHolder = new StakeHolder(customer.getCustomerName(), customer.getStakeHolderCategoryId());
        stakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
        customer.setStakeHolderId(stakeHolder.getStakeHolderId());
        subscriptionService.subscribeToStatuses(customer.getStakeHolderId(), customer.getDefaultSubscriptionStatusIds());
        subscriptionService.subscribeToChannels(customer.getStakeHolderId(), customer.getDefaultSubscriptionChannelIds());
        return customerRepo.save(customer);
    }
}
