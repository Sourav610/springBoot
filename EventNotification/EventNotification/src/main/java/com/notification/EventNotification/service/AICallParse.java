package com.notification.EventNotification.service;

import com.notification.EventNotification.dto.ParseRemainderDTO;
import com.notification.EventNotification.dto.SetAlertRequest;
import org.springframework.http.ResponseEntity;

public interface AICallParse {
    SetAlertRequest toPreview(ParseRemainderDTO alert,String userEmail);
    ResponseEntity<?> parseAlertText(String rawText, String userEmail);
}
