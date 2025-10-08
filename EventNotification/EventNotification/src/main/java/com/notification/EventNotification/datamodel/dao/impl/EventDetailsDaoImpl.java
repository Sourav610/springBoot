package com.notification.EventNotification.datamodel.dao.impl;


import com.notification.EventNotification.datamodel.dao.EventDetailsDao;
import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import com.notification.EventNotification.datamodel.repository.EventDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDetailsDaoImpl implements EventDetailsDao {

    @Autowired
    EventDetailsRepo eventDetailsRepo;

    public void save(EventDataEntity eventDataEntity) {
        eventDetailsRepo.save(eventDataEntity);
    }

    @Override
    public List<EventDataEntity> findAll() {
        return eventDetailsRepo.findAll();
    }

    @Override
    public List<EventDataEntity> findByUserId(int userId) {
        return eventDetailsRepo.findByUserId(userId);
    }
}
