package com.notification.notification.service.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotifier implements NotificationObserver {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotifier.class);

    @Override
    public void update(String message) {
        displayOnConsole(message);
    }

    @Override
    public String getType() {
        return "CONSOLE";
    }

    private void displayOnConsole(String message) {
        log.info("CONSOLE NOTIFICATION: {}", message);
        System.out.println("═══════════════════════════════════════");
        System.out.println("NOTIFICATION: " + message);
        System.out.println("═══════════════════════════════════════");
    }
}