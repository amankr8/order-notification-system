package com.flykraft.service.notification;

import java.time.LocalDateTime;

public class SMSService implements CommService {

    @Override
    public void sendNotification(String notification) {
        System.out.println("[SMS]\t[" + LocalDateTime.now() + "][NOTIFICATION]" + notification);
    }
}
