package com.notification.EventNotification.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date  createdOn;
    private String created_by;
    private Date updatedOn;
    private String notificationType;
    private int alertLimit;
    private int eventDetailsId;

}
