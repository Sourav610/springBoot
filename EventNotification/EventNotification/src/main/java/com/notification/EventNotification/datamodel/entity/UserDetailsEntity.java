package com.notification.EventNotification.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class UserDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private Date createdOn;
    private Date updatedOn;
    private Date loginTime;
    private Date logoutTime;
}
