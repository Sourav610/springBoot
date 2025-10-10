package com.notification.EventNotification.controller;

import com.notification.EventNotification.dto.SetAlertRequest;
import com.notification.EventNotification.service.impl.SetAlertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class SetAlertController {

    @Autowired
    SetAlertServiceImpl setAlertService;

    @PostMapping("/createAlert")
    public ResponseEntity<?> setAlert(@RequestBody SetAlertRequest setAlertRequest){
        log.info("inside set alert controller..");
        return setAlertService.pushAlertData(setAlertRequest);
    }

    @PostMapping("/viewAlerts")
    public ResponseEntity<?>getAlerts(@RequestBody SetAlertRequest setAlertRequest){
        log.info("inside get alert controller for email : {}", setAlertRequest.getPersonEmail());
        return setAlertService.getAlertData(setAlertRequest.getPersonEmail());
    }
}
