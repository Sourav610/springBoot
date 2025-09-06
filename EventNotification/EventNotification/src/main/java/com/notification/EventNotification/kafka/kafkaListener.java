package com.notification.EventNotification.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class kafkaListener {

    @KafkaListener(id="receiver",topics = "${kafka.topic}")
    public void listener(String data){
        log.info("Listening from kafka server..");
        log.info("the recived data is {}",data);

    }
}
