package com.notification.EventNotification.datamodel.repository;

import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetailsEntity,Integer> {
    UserDetailsEntity findByEmail(String email);
}
