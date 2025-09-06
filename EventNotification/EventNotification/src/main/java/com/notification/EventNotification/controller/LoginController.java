package com.notification.EventNotification.controller;

import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserDetailDAO userDetailDAO;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        log.info("Inside login request for email: {}",loginRequest.getEmail());
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        if(email.isEmpty() || password.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }

        UserDetailsEntity userDetails = userDetailDAO.findByEmail(email);
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        if(!userDetails.getPassword().equals(password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong Password");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
    }
}
