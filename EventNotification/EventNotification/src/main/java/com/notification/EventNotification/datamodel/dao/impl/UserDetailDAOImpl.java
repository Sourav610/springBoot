package com.notification.EventNotification.datamodel.dao.impl;

import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.datamodel.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailDAOImpl implements UserDetailDAO {

    private final UserDetailRepository userDetailRepository;

    @Override
    public UserDetailsEntity findByEmail(String email) {
        return userDetailRepository.findByEmail(email);
    }
}
