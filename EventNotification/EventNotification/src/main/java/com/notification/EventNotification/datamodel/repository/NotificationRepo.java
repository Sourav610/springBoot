package com.notification.EventNotification.datamodel.repository;

import com.notification.EventNotification.datamodel.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<NotificationEntity,Integer> {
    List<NotificationEntity> findByEventDetailsId(int id);
}
