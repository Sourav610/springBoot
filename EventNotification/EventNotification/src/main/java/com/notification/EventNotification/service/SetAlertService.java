package com.notification.EventNotification.service;

import com.notification.EventNotification.dto.SetAlertRequest;
import org.springframework.http.ResponseEntity;

public interface SetAlertService {
     ResponseEntity<?> pushAlertData(SetAlertRequest setAlertRequest);

     ResponseEntity<?> getAlertData(String userEmail);
}
