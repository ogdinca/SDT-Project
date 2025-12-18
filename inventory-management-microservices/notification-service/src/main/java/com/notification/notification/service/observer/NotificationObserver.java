package com.notification.notification.service.observer;

public interface NotificationObserver {
    void update(String message);
    String getType();
}