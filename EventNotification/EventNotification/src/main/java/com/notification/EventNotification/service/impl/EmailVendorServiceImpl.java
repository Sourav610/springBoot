package com.notification.EventNotification.service.impl;

import com.mailjet.client.errors.MailjetException;
import com.notification.EventNotification.service.EmailVendorService;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import com.notification.EventNotification.util.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailVendorServiceImpl implements EmailVendorService {

    @Autowired
    private ApiResponseUtil apiResponseUtil;
    @Override
    public ResponseEntity<?> callEmailVendor(String to, String message, String from, String subject) throws MailjetException {
        log.info("Calling email for sending notification..");
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(System.getenv("MJ_APIKEY_PUBLIC"), System.getenv("MJ_APIKEY_PRIVATE"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", from))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.HTMLPART, message)));
        response = client.post(request);
        if(response.getStatus() == 200){
            return apiResponseUtil.createResponse("Message sent",response.getRawResponseContent(),response.getStatus());
        }
        return apiResponseUtil.createResponse("Send failed",response.getRawResponseContent(),response.getStatus());

    }
}
