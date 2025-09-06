package com.notification.EventNotification.datamodel.repository;

import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDetailsRepo extends JpaRepository<EventDataEntity,Integer> {
    List<EventDataEntity> findAll();
}
