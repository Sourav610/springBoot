package com.notification.EventNotification.util;

import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {
    ResponseEntity<?> createResponse(String message, Object data, int statusCode);
}
