package com.notification.EventNotification.service;

public interface EmailVendorService {
    String callEmailVendor(String to, String message, String from);
}
