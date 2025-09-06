package com.notification.EventNotification.datamodel.dao.impl;

import com.notification.EventNotification.datamodel.dao.NotificationDAO;
import com.notification.EventNotification.datamodel.entity.NotificationEntity;
import com.notification.EventNotification.datamodel.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationDAOImpl implements NotificationDAO {

    @Autowired
    private NotificationRepo notificationRepo;
    @Override
    public List<NotificationEntity> findByEventDetailsId(int id) {
        return notificationRepo.findByEventDetailsId(id);
    }
}
