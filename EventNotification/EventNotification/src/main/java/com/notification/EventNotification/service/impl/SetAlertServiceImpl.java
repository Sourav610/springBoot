package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.EventDetailsDao;
import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.dao.impl.EventDetailsDaoImpl;
import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.dto.SetAlertRequest;
import com.notification.EventNotification.service.SetAlertService;
import com.notification.EventNotification.util.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
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
        String email = setAlertRequest.getNotifierEmail();
        String eventTitle = setAlertRequest.getEventTitle();
        String eventMessage = setAlertRequest.getEventMessage();
        String userEmail = setAlertRequest.getUserEmail();
        SetAlertRequest.APIType apiType = setAlertRequest.getApiType();
        Integer eventId = setAlertRequest.getEventId();


        if(personName == null || eventDate == null|| eventType == null || eventTitle == null || eventMessage == null){
            return apiResponseUtil.createResponse("All fields are required", null,400);
        }


        UserDetailsEntity userDetails = userDetailDAO.findByEmail(userEmail);
        if(userDetails == null){
            return apiResponseUtil.createResponse("User not found with email: " + email,null,400);
        }

        EventDataEntity eventDataEntity;
        if(apiType.equals(SetAlertRequest.APIType.UPDATE)){
            if(eventId== null){
                return apiResponseUtil.createResponse("Id is required for updating event",null,400);
            }
            eventDataEntity = eventDetailsDao.findById(eventId);
        }
        else{
            eventDataEntity = new EventDataEntity();
            eventDataEntity.setCreatedOn(new Date());
        }

        if(eventType.equalsIgnoreCase("SMS")){
            if(setAlertRequest.getNotifierNumber() == null){
                return apiResponseUtil.createResponse("Mobile number is required for SMS alert",null,400);
            }
            mobileNo = setAlertRequest.getNotifierNumber();
            eventDataEntity.setMobileNumber(mobileNo);
        }
        else{
            if(setAlertRequest.getNotifierEmail() == null){
                return apiResponseUtil.createResponse("Email is required for Email alert",null,400);
            }
            eventDataEntity.setEmail(email);
        }

        eventDataEntity.setEventDate(eventDate);
        eventDataEntity.setPersonName(personName);
        eventDataEntity.setUserId(userDetails.getId());
        eventDataEntity.setEventMessage(eventMessage);
        eventDataEntity.setEventTitle(eventTitle);
        eventDataEntity.setEventType(eventType);
        eventDataEntity.setUpdatedOn(new Date());

        try {
            eventDetailsDao.save(eventDataEntity);
        }catch(Exception e){
            return apiResponseUtil.createResponse("Error saving event data",null,500);
        }
        return apiResponseUtil.createResponse("Alert save successfully",null,200);
    }

    @Override
    public ResponseEntity<?> getAlertData(String userEmail) {
        if(userEmail == null){
            return apiResponseUtil.createResponse("Email is required",null,400);
        }
        UserDetailsEntity userDetails = userDetailDAO.findByEmail(userEmail);
        if(userDetails == null){
            return apiResponseUtil.createResponse("User not found with email: " + userEmail,null,400);
        }

        List<EventDataEntity> eventList = eventDetailsDao.findByUserId(userDetails.getId());
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("events",eventList);
        return apiResponseUtil.createResponse("Event list fetched successfully",responseMap,200);
    }

    @Override
    public ResponseEntity<?> deleteAlerts(Integer id) {
        log.info("Inside delete alert service for eventId:{}",id);
        eventDetailsDao.deleteById(id);
        return apiResponseUtil.createResponse("Alert deleted successfully",null,200);
    }


}
