package com.notification.EventNotification.dto;

import lombok.Data;

@Data
public class NotificationContext {
    private String name;
    private String channelId;
    private String toId;
    private String fromId;
    private String messageBody;
}
