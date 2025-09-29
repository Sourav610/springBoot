package com.notification.EventNotification.util.impl;

import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
    public String generateToken(UserDetailsEntity userDetails) {
        return Jwts.builder()
                .claim("email", userDetails.getEmail())
                .claim("username",userDetails.getFullName())
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + expirationTime)) // 10 hours
                .signWith(key)
                .compact();
    }

    @Override
    public Map<String,Object> getPayload(String token) {
        log.info("Inside the getPayload service..");
        int index = token.indexOf("Bearer ");
        if(index == -1) {
            return null;
        }
        token = token.substring(index+7);
        try {
            Claims claims = Jwts.parser()
                    .decryptWith((SecretKey)key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


            Map<String, Object> payload = new HashMap<>();
            payload.put("subject", claims.getSubject());
            payload.put("expiration", claims.getExpiration());
            return payload;
        }catch (ExpiredJwtException e){
            log.warn("Token is expired, but returning claims...");
            return buildPayload(e.getClaims());
        }
        catch (Exception e) {
            log.error("Error getting subject from token: ", e);
            return null;
        }
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

    private Map<String, Object> buildPayload(Claims claims) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("subject", claims.getSubject());
        payload.put("expiration", claims.getExpiration());
        return payload;
    }
}
