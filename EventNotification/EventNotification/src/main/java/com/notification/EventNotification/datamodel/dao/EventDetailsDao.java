package com.notification.EventNotification.datamodel.dao;

import com.notification.EventNotification.datamodel.entity.EventDataEntity;

import java.util.List;

public interface EventDetailsDao {
    List<EventDataEntity> findAll();
}
