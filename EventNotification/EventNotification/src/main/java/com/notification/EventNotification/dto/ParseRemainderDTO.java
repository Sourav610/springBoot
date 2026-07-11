package com.notification.EventNotification.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParseRemainderDTO {
    private String title;
    private String description;
    private String date;
    private String time;
    private String recurrence;
    private String category;
    private String priority;
    private String confidence;
    private String notes;
}