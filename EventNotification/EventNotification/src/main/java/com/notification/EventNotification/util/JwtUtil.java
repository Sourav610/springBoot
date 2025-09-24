package com.notification.EventNotification.util;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface JwtUtil {
    String generateToken(String email);
    Map<String, Object> getPayload(String token);
    boolean validateToken(String token, String email);
}
