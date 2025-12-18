package com.flykraft.service.stakeholder;

import com.flykraft.model.stakeholder.*;
import com.flykraft.repository.stakeholder.StakeHolderRepo;

public class StakeHolderService {

    private final StakeHolderRepo stakeHolderRepo;

    public StakeHolderService(StakeHolderRepo stakeHolderRepo) {
        this.stakeHolderRepo = stakeHolderRepo;
    }

    public StakeHolder getStakeHolderById(Integer id) {
        return stakeHolderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder does not exist"));
    }

    public StakeHolder createStakeHolder(StakeHolder stakeHolder) {
        return stakeHolderRepo.save(stakeHolder);
    }

    public void optInForNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = getStakeHolderById(stakeHolderId);
        stakeHolder.setOptedInForNotifications(true);
        stakeHolderRepo.save(stakeHolder);
    }

    public void optOutOfNotifications(Integer stakeHolderId) {
        StakeHolder stakeHolder = getStakeHolderById(stakeHolderId);
        stakeHolder.setOptedInForNotifications(false);
        stakeHolderRepo.save(stakeHolder);
    }
}
