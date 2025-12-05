package com.notification.notification.service.model;

public class NotificationRequest {

    private String message;
    private String type; // e.g., "EMAIL", "CONSOLE", "SMS"

    // Default constructor
    public NotificationRequest() {
    }

    // Constructor with message only
    public NotificationRequest(String message) {
        this.message = message;
        this.type = "ALL"; // Default to all channels
    }

    // Constructor with all fields
    public NotificationRequest(String message, String type) {
        this.message = message;
        this.type = type;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}