package com.notification.EventNotification.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="event_details")
public class EventDataEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private Date eventDate;
    private String personName;
    private String mobileNumber;
    private String eventType;


}
