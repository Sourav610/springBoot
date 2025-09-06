package com.notification.EventNotification.util.impl;

import com.notification.EventNotification.dto.NotificationContext;
import com.notification.EventNotification.util.NotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class NotificationUtilImpl implements NotificationUtil {

    @Autowired
    private KafkaTemplate<String,String> sender;

    @Value("${kafka.sender.topic}")
    private String kafkaTopic;

    @Override
    public void sendNotification(NotificationContext notificationContext) {
        log.info("Sending notification to kafka topic");
        try{
            sender.send(kafkaTopic, String.valueOf(notificationContext));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
