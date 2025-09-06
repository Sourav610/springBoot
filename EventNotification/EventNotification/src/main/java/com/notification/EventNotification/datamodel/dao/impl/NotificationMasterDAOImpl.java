package com.notification.EventNotification.datamodel.dao.impl;

import com.notification.EventNotification.datamodel.dao.NotificationMasterDAO;
import com.notification.EventNotification.datamodel.entity.NotificationMaster;
import com.notification.EventNotification.datamodel.repository.NotificationMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Component;

@Component
public class NotificationMasterDAOImpl implements NotificationMasterDAO {

    @Autowired
    private NotificationMasterRepo notificationMasterRepo;
    @Override
    public NotificationMaster findByNotificationType(String type) {
        return notificationMasterRepo.findByNotificationType(type);
    }
}
