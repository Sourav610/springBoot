package com.notification.EventNotification.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SetAlertRequest {
    private Date eventDate;
    private String personName;
    private String notifierNumber;
    private String eventType;
    private String eventTitle;
    private String eventMessage;
    private String notifierEmail;
    private Date createdOn;
    private Date updatedOn;
    private String userEmail;
}
