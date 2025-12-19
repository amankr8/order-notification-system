package com.flykraft.service.notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SMSService implements CommService {

    @Override
    public void sendNotification(String notification) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("[SMS]\t[NOTIFICATION][" + LocalDateTime.now().format(formatter) + "]" + notification);
    }
}
