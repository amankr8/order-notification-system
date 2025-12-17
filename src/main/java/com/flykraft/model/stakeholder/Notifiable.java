package com.flykraft.model.stakeholder;

import java.util.Set;

public interface Notifiable {

    Set<Integer> getDefaultStatusIds();

    Set<Integer> getDefaultChannelIds();
}
