package com.flykraft.model.notification;

import com.flykraft.service.notification.CommService;
import com.flykraft.service.notification.EmailService;
import com.flykraft.service.notification.SMSService;

public enum Channel {
    EMAIL(1, "Email", new EmailService()),
    SMS(2, "SMS", new SMSService());

    private final Integer id;
    private final String name;
    private final CommService service;

    Channel(Integer id, String name, CommService service) {
        this.id = id;
        this.name = name;
        this.service = service;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CommService getService() {
        return service;
    }

    public static Channel getChannelById(Integer id) {
        for (Channel type : Channel.values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }
}
