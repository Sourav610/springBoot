package com.notification.EventNotification.datamodel.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class UserDetailsEntity {
    String fullName;
    String email;
    String password;
}
