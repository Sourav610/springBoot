package com.notification.EventNotification.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.notification.EventNotification.datamodel.dao.JwtBlackListDAO;
import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.JwtBlackList;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.service.UserAuthService;
import com.notification.EventNotification.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.json.JSONObject;
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

    private final JwtBlackListDAO jwtBlackListDAO;
    @Override
    public ResponseEntity<?> login(String email, String password) {
        log.info("Inside the login service for email: {}",email);
        try{
            UserDetailsEntity userDetails = userDetailDAO.findByEmail(email);
            if(userDetails == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
            }

            if(!userDetails.getPassword().equals(password)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong Password");
            }

            String token = jwtUtil.generateToken(userDetails);
            userDetails.setOnline(true);
            userDetailDAO.save(userDetails);
            Map<String,Object> response = new HashMap<>();
            response.put("token",token);
            response.put("code",200);
            response.put("message","User logged in successfully");
            response.put("status","success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e){
            log.error("Error during login for email: {}",email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }

    }

    @Override
    public ResponseEntity<?> register(String fullName, String email, String password) {
        log.info("Inside the register service for email: {}",email);
        UserDetailsEntity existingUser = userDetailDAO.findByEmail(email);
        if(existingUser != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
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
    public ResponseEntity<?> logout(String token) {
        log.info("Inside the logout service");
        try{
            Map<String,Object> tokenPayload = jwtUtil.getPayload(token);
            if (tokenPayload == null) {
                return ResponseEntity.badRequest().body("Invalid token");
            }
            JSONObject userData = new JSONObject(tokenPayload);
            JSONObject subject = new JSONObject(userData.getString("subject"));
            Date expiration = (Date) tokenPayload.get("expiration");
            if (expiration.before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
            }
            UserDetailsEntity userDetails = userDetailDAO.findByEmail(subject.get("email").toString());
            if(userDetails == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            JwtBlackList jwtBlackList = new JwtBlackList();
            jwtBlackList.setToken(token);
            jwtBlackList.setCreatedOn(new Date());
            jwtBlackList.setExpiryDate(expiration);
            jwtBlackListDAO.save(jwtBlackList);

            userDetails.setOnline(false);
            userDetailDAO.save(userDetails);
            Map<String,Object> response = new HashMap<>();
            response.put("code",200);
            response.put("message","User logged out successfully");
            response.put("status","success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e){
            log.error("Error during logout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout");
        }
    }
}
