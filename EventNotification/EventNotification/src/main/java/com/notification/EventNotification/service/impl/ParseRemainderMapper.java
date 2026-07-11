package com.notification.EventNotification.service.impl;

import com.notification.EventNotification.datamodel.dao.UserDetailDAO;
import com.notification.EventNotification.datamodel.entity.UserDetailsEntity;
import com.notification.EventNotification.dto.ParseRemainderDTO;
import com.notification.EventNotification.dto.SetAlertRequest;
import com.notification.EventNotification.service.AICallParse;
import com.notification.EventNotification.util.ApiResponseUtil;
import com.notification.EventNotification.util.impl.RemainderParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class ParseRemainderMapper implements AICallParse {

    @Autowired
    private UserDetailDAO userDetailDAO;

    @Autowired
    private ApiResponseUtil apiResponseUtil;

    @Autowired
    private GeminiRemainderParser geminiRemainderParser;

    private static final LocalTime DEFAULT_TIME = LocalTime.of(9, 0);

    @Override
    public SetAlertRequest toPreview(ParseRemainderDTO parsed, String userEmail) {
        SetAlertRequest preview = new SetAlertRequest();
        preview.setEventTitle(parsed.getTitle());
        preview.setEventMessage(parsed.getDescription());
        preview.setCategory(parsed.getCategory());
        preview.setPriority(parsed.getPriority());
        preview.setConfidence(parsed.getConfidence());
        preview.setEventMessage(parsed.getNotes());
        preview.setNotifierEmail(userEmail);
        preview.setEventDate(combineDateAndTime(parsed.getDate(), parsed.getTime()));

        return preview;
    }

    @Override
    public ResponseEntity<?> parseAlertText(String rawText, String userEmail) {
        if (rawText == null || rawText.isBlank()) {
            return apiResponseUtil.createResponse("Text input is required", null, 400);
        }
        if (userEmail == null || userEmail.isBlank()) {
            return apiResponseUtil.createResponse("User email is required", null, 400);
        }

//        UserDetailsEntity userDetails = userDetailDAO.findByEmail(userEmail);
//        if (userDetails == null) {
//            return apiResponseUtil.createResponse("User not found with email: " + userEmail, null, 400);
//        }

        try {
            ParseRemainderDTO parsed = geminiRemainderParser.parseReminder(rawText);
            SetAlertRequest preview = toPreview(parsed, userEmail);
            return apiResponseUtil.createResponse("Parsed successfully", preview, 200);
        } catch (RemainderParserException e) {
            log.error("Gemini parse failed for user {}: {}", userEmail, e.getMessage());
            return apiResponseUtil.createResponse(e.getMessage(), null, 500);
        }
    }

    private LocalDateTime combineDateAndTime(String dateStr, String timeStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null; // no date at all — frontend must force manual entry / low confidence flow
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalTime time = DEFAULT_TIME;

            if (timeStr != null && !timeStr.isBlank()) {
                try {
                    time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
                } catch (DateTimeParseException e) {
                    log.warn("Unparseable time '{}', falling back to default 09:00", timeStr);
                }
            }

            return LocalDateTime.of(date, time);

        } catch (DateTimeParseException e) {
            log.warn("Unparseable date '{}', returning null eventDate", dateStr);
            return null;
        }
    }
}
