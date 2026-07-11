package com.notification.EventNotification.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SetAlertRequest {
    private LocalDateTime eventDate;
    private String category;
    private String priority;
    private String confidence;
    private String personName;
    private String notifierNumber;
    private String eventType;
    private String eventTitle;
    private String eventMessage;
    private String notifierEmail;
    private Date createdOn;
    private Date updatedOn;
    private String userEmail;
    private APIType apiType;
    private Integer eventId;
    private String text;

    public enum APIType{
        CREATE,UPDATE
    }
}
