package com.notification.EventNotification.util;

import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface JwtUtil {
    String generateToken(UserDetailsEntity userDetails);
    Map<String, Object> getPayload(String token);
    boolean validateToken(String token, String email);
}
