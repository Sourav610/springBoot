package com.notification.EventNotification.service;

import org.springframework.http.ResponseEntity;

public interface UserAuthService {
    ResponseEntity<?> login(String email, String password);
    ResponseEntity<?> register(String fullName, String email, String password);

    ResponseEntity<?> logout(String token);
}
