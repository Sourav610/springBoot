package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.EventDetailsDao;
import com.notification.EventNotification.datamodel.dao.impl.EventDetailsDaoImpl;
import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import com.notification.EventNotification.dto.SetAlertRequest;
import com.notification.EventNotification.service.SetAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SetAlertServiceImpl implements SetAlertService {
    @Autowired
    EventDetailsDaoImpl eventDetailsDao;
    @Override
    public String pushAlertData(SetAlertRequest setAlertRequest) {
        Date eventDate = setAlertRequest.getEventDate();
        String personName = setAlertRequest.getPersonName();
        String mobileNo = setAlertRequest.getMobileNumber();
        String eventType = setAlertRequest.getEventType();

        if(personName == null || eventDate == null || mobileNo == null || eventType == null){
            return "Invalid Request";
        }
        EventDataEntity eventDataEntity = new EventDataEntity();
        eventDataEntity.setEventDate(eventDate);
        eventDataEntity.setPersonName(personName);
        eventDataEntity.setMobileNumber(mobileNo);

        try {
            eventDetailsDao.save(eventDataEntity);
        }catch(Exception e){
            return e.getMessage();
        }
        return "Data pushed Successfully";
    }
}
