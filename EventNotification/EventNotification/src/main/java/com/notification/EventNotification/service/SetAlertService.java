package com.notification.EventNotification.service;

import com.notification.EventNotification.dto.SetAlertRequest;

public interface SetAlertService {
    public String pushAlertData(SetAlertRequest setAlertRequest);
}
