package com.notification.EventNotification.datamodel.dao;

import com.notification.EventNotification.datamodel.entity.NotificationEntity;

import java.util.List;

public interface NotificationDAO {
    List<NotificationEntity> findByEventDetailsId(int id);
    NotificationEntity findById(Integer id);

    void save(NotificationEntity notification);
}
