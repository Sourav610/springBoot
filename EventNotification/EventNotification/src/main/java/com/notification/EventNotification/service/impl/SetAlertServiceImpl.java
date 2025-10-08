package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.EventDetailsDao;
import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.dao.impl.EventDetailsDaoImpl;
import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.dto.SetAlertRequest;
import com.notification.EventNotification.service.SetAlertService;
import com.notification.EventNotification.util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SetAlertServiceImpl implements SetAlertService {
    @Autowired
    EventDetailsDaoImpl eventDetailsDao;
    @Autowired
    private UserDetailDAO userDetailDAO;

    @Autowired
    private ApiResponseUtil apiResponseUtil;
    @Override
    public ResponseEntity<?> pushAlertData(SetAlertRequest setAlertRequest) {
        Date eventDate = setAlertRequest.getEventDate();
        String personName = setAlertRequest.getPersonName();
        String mobileNo;
        String eventType = setAlertRequest.getEventType();
        String email = setAlertRequest.getPersonEmail();
        String eventTitle = setAlertRequest.getEventTitle();
        String eventMessage = setAlertRequest.getEventMessage();

        if(personName == null || eventDate == null|| eventType == null || email == null || eventTitle == null || eventMessage == null){
            return apiResponseUtil.createResponse("All fields are required", null,400);
        }

        UserDetailsEntity userDetails = userDetailDAO.findByEmail(email);
        if(userDetails == null){
            return apiResponseUtil.createResponse("User not found with email: " + email,null,400);
        }
        EventDataEntity eventDataEntity = new EventDataEntity();
        if(eventType.equalsIgnoreCase("SMS")){
            if(setAlertRequest.getMobileNumber() == null){
                return apiResponseUtil.createResponse("Mobile number is required for SMS alert",null,400);
            }
            mobileNo = setAlertRequest.getMobileNumber();
            eventDataEntity.setMobileNumber(mobileNo);
        }
        else{
            if(setAlertRequest.getPersonEmail() == null){
                return apiResponseUtil.createResponse("Email is required for Email alert",null,400);
            }
            eventDataEntity.setEmail(email);
        }

        eventDataEntity.setEventDate(eventDate);
        eventDataEntity.setPersonName(personName);
        eventDataEntity.setUserId(userDetails.getId());
        eventDataEntity.setEventMessage(eventMessage);
        eventDataEntity.setEventTitle(eventTitle);

        try {
            eventDetailsDao.save(eventDataEntity);
        }catch(Exception e){
            return apiResponseUtil.createResponse("Error saving event data",null,500);
        }
        return apiResponseUtil.createResponse("Alert set successfully",null,200);
    }
}
