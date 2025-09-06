package com.notification.EventNotification.datamodel.dao;

import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;

public interface UserDetailDAO {
    UserDetailsEntity findByEmail(String email);
}
