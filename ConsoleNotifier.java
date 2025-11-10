package com.example.project2;

public class ConsoleNotifier implements InventoryObserver {
    @Override
    public void update(String message) {
        System.out.println("Console Notification: " + message);
    }
}
