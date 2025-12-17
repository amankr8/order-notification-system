package com.flykraft.model.stakeholder;

import java.util.Set;

public interface Notifiable {

    Set<Integer> getDefaultSubscriptionStatusIds();

    Set<Integer> getDefaultSubscriptionChannelIds();
}
