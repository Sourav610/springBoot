package com.notification.EventNotification.util.impl;

import com.notification.EventNotification.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class JwtUtilImpl implements JwtUtil {

    private final Key key;
    @Value("${jwt.expiration.time}")
    private long expirationTime;

    public JwtUtilImpl(@Value("${jwt.secret.key}") String keyVal) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(keyVal));
    }

    @Override
    public String generateToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + expirationTime)) // 10 hours
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token, String email) {
        return Jwts.parser()
                .decryptWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class)
                .equals(email);
    }
}
