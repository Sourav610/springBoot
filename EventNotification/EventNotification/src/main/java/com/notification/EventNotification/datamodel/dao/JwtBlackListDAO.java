package com.notification.EventNotification.datamodel.dao;


import com.notification.EventNotification.datamodel.entity.JwtBlackList;

public interface JwtBlackListDAO {
    public void save(JwtBlackList jwtBlackList);
    public JwtBlackList findByToken(String token);
}
