package com.example.project2;

public class EmailNotifier implements InventoryObserver {
    @Override
    public void update(String message) {
        System.out.println("Email Notification: " + message);
    }
}