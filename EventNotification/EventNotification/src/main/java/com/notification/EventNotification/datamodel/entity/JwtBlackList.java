package com.notification.EventNotification.datamodel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Generated;

import java.util.Date;

@Data
@Entity
public class JwtBlackList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;
    String token;
    Date createdOn;
    Date expiryDate;
}
