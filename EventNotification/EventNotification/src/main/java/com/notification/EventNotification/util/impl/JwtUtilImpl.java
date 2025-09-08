package com.notification.EventNotification.util.impl;

import com.notification.EventNotification.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class JwtUtilImpl implements JwtUtil {
    @Override
    public String generateToken(String email) {
        return Jwts.builder()
                .claim("email", email);
    }

    @Override
    public boolean validateToken(String token, String email) {
        return false;
    }
}
