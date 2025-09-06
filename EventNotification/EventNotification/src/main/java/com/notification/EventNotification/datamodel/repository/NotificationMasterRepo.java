package com.notification.EventNotification.datamodel.repository;

import com.notification.EventNotification.datamodel.entity.NotificationMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationMasterRepo extends JpaRepository<NotificationMaster, Integer> {
    NotificationMaster findByNotificationType(String type);
}
