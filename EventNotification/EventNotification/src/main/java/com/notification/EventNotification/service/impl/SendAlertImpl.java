package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.EventDetailsDao;
import com.notification.EventNotification.datamodel.dao.NotificationDAO;
import com.notification.EventNotification.datamodel.dao.NotificationMasterDAO;
import com.notification.EventNotification.datamodel.entity.EventDataEntity;
import com.notification.EventNotification.datamodel.entity.NotificationMaster;
import com.notification.EventNotification.service.SendAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.abs;

@Component
@Slf4j
public class SendAlertImpl implements SendAlert {

    @Autowired
    private EventDetailsDao eventDetailsDao;

    @Autowired
    private NotificationMasterDAO notificationMasterDAO;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private KafkaTemplate<String,String>sender;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${sender.email}")
    private String fromEmail;

    @Override
    @Scheduled(cron="${scheduler.time}")
    public String pushNotification() {
        log.info("Pushing notification to kafka server");
        List<EventDataEntity> newEvent = eventDetailsDao.findAll();
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        SimpleDateFormat smf = new SimpleDateFormat("YYYY:MM:DD");
        Calendar cal = Calendar.getInstance();
        for(int i = 0; i<newEvent.size(); i++){
            EventDataEntity eventDetail = newEvent.get(i);
            String Type = eventDetail.getEventType();
            LocalDateTime nowIst = LocalDateTime.now(zone);
            LocalDateTime eventIst = eventDetail.getEventDate();
            long diff = Math.abs(Duration.between(nowIst, eventIst).toMillis());

            try {
                if (diff<=1000) {
                    NotificationMaster notificationMaster = notificationMasterDAO.findByNotificationType(Type);
                    if (notificationMaster == null) {
                        throw new RuntimeException("Template not present");
                    }
                    String message = notificationMaster.getNotificationTemplate();
                    message = message.replace("$Name", eventDetail.getPersonName());
                    Map<String,Object> newMessage = createKafkaMessage("EMAIL",fromEmail, eventDetail.getMobileNumber(),null, message);
                    log.info("The message is: "+message);
                    sender.send(kafkaTopic,newMessage.toString());
                    log.info("Data pushed successfully");
                }
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        return "Notification send successfully";
    }

    public Map<String,Object> createKafkaMessage(String channel, String fromId, String toId, String subject, String message){
        Map<String, Object> kafkaMessage = new HashMap<>();
        kafkaMessage.put("fromId",fromId);
        kafkaMessage.put("toId",toId);
        kafkaMessage.put("subject",subject);
        kafkaMessage.put("message",message);
        kafkaMessage.put("channel",channel);
        return kafkaMessage;
    }
}
