package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.service.UserAuthService;
import com.notification.EventNotification.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtUtil jwtUtil;

    private final UserDetailDAO userDetailDAO;
    @Override
    public ResponseEntity<?> login(String email, String password) {
        UserDetailsEntity userDetails = userDetailDAO.findByEmail(email);
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        if(!userDetails.getPassword().equals(password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong Password");
        }

        String token = jwtUtil.generateToken(email);
        Map<String,Object> response = new HashMap<>();
        response.put("token",token);
        response.put("code",200);
        response.put("message","User logged in successfully");
        response.put("status","success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> register(String fullName, String email, String password) {
        log.info("Inside the register service for email: {}",email);
        UserDetailsEntity newUser = new UserDetailsEntity();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setCreatedOn(new Date());
        newUser.setUpdatedOn(new Date());
        newUser.setOnline(false);

        userDetailDAO.save(newUser);
        Map<String,Object>response = new HashMap<>();
        response.put("code",200);
        response.put("message","User registered successfully");
        response.put("status","success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> logout(String email) {
        return null;
    }
}
