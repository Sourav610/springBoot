package com.notification.EventNotification.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SetAlertRequest {
    private Date eventDate;
    private String personName;
    private String mobileNumber;
    private String eventType;
    private String eventTitle;
    private String eventMessage;
    private String email;
}
