package com.flykraft.service.notification;

import java.time.LocalDateTime;

public class EmailService implements CommService {

    @Override
    public void sendNotification(String message) {
        System.out.println("[EMAIL][" + LocalDateTime.now() + "][NOTIFICATION] - " + message);
    }
}
