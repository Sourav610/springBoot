package com.notification.EventNotification.datamodel.dao;

import com.notification.EventNotification.datamodel.entity.EventDataEntity;

import java.util.List;

public interface EventDetailsDao {
    List<EventDataEntity> findAll();

    List<EventDataEntity> findByUserId(int userId);

    EventDataEntity findById(int id);

    void deleteById(int id);
}
