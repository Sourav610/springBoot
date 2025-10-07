package com.notification.EventNotification.util.impl;

import com.notification.EventNotification.util.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ApiResponseUtilImpl implements ApiResponseUtil {
    @Override
    public ResponseEntity<?> createResponse(String message, Object data, int statusCode) {
        Map<String,Object> finalResponse = new HashMap<>();
        finalResponse.put("message", message);
        finalResponse.put("code",statusCode);
        finalResponse.put("responseData", data);
        return ResponseEntity.ok(finalResponse);
    }
}
