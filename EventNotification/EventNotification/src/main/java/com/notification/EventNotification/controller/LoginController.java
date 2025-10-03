package com.notification.EventNotification.controller;

import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.dto.LoginRequest;
import com.notification.EventNotification.service.UserAuthService;
import com.notification.EventNotification.util.JwtUtil;
import com.notification.EventNotification.util.impl.JwtUtilImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping
@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserAuthService userAuthService;
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        log.info("Inside login request for email: {}",loginRequest.getEmail());
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        if(email.isEmpty() || password.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }

        return userAuthService.login(email,password);

    }


    @PostMapping("/signup")
    public ResponseEntity<?>userSignup(@RequestBody LoginRequest loginRequest) {
        log.info("Inside signup request for email: {}", loginRequest.getEmail());
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            String fullName = loginRequest.getFullName();
            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
            }

            return userAuthService.register(fullName, email, password);
        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during signup");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?>logout(@RequestBody String token){
        log.info("Inside logout request..");
        return userAuthService.logout(token);
    }
}
