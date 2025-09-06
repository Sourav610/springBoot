package com.notification.EventNotification.datamodel.dao;

import com.notification.EventNotification.datamodel.entity.NotificationMaster;

public interface NotificationMasterDAO {
    NotificationMaster findByNotificationType(String type);
}
