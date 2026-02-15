package com.notification.EventNotification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailjet.client.errors.MailjetException;
import com.notification.EventNotification.service.EmailVendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class kafkaListener {

    private final EmailVendorService emailVendorService;

    @KafkaListener(id="receiver",topics = "${kafka.topic}")
    public void listener(String data) throws JsonProcessingException, MailjetException {
        log.info("Listening from kafka server..");
        log.info("the recived data is {}",data);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(data);
        String fromId = node.get("from").toString();
        String toId = node.get("to").toString();
        String subject = node.get("subject").toString();
        String message = node.get("message").toString();
        String channel = node.get("channel").toString();
        ResponseEntity<?> response;
        if(channel.equals("EMAIL")){
             response = emailVendorService.callEmailVendor(toId,message,fromId,subject);
        }

    }
}
