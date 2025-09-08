package com.notification.EventNotification.util;

public interface JwtUtil {
    String generateToken(String email);
    boolean validateToken(String token, String email);
}
