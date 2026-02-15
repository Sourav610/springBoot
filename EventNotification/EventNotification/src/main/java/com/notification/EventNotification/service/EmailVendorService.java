package com.notification.EventNotification.service;

import com.mailjet.client.errors.MailjetException;
import org.springframework.http.ResponseEntity;

public interface EmailVendorService {
    ResponseEntity<?> callEmailVendor(String to, String message, String from, String subject) throws MailjetException;
}
