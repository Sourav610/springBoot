package com.notification.EventNotification.datamodel.repository;

import com.notification.EventNotification.datamodel.entity.JwtBlackList;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Integer> {
    JwtBlackList findByToken(String token);
}
