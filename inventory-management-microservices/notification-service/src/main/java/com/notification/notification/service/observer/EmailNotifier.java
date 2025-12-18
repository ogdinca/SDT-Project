package com.notification.notification.service.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailNotifier implements NotificationObserver {

    private static final Logger log = LoggerFactory.getLogger(EmailNotifier.class);

    @Override
    public void update(String message) {
        // In a real application, this would send an actual email
        // For now, we'll simulate it with logging
        sendEmail(message);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }

    private void sendEmail(String message) {
        log.info("EMAIL NOTIFICATION SENT: {}", message);
        // TODO: Integrate with actual email service (SendGrid, AWS SES, etc.)
        // Example:
        // emailService.send("admin@inventory.com", "Inventory Update", message);
    }
}