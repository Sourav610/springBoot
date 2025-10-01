package com.notification.EventNotification.datamodel.dao.impl;

import com.notification.EventNotification.datamodel.dao.JwtBlackListDAO;
import com.notification.EventNotification.datamodel.entity.JwtBlackList;
import com.notification.EventNotification.datamodel.repository.JwtBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtBlackListDAOImpl implements JwtBlackListDAO {

    @Autowired
    private JwtBlackListRepository jwtBlackListRepository;

    @Override
    public void save(JwtBlackList jwtBlackList) {
        jwtBlackListRepository.save(jwtBlackList);
    }

    @Override
    public JwtBlackList findByToken(String token) {
        return jwtBlackListRepository.findByToken(token);
    }
}
