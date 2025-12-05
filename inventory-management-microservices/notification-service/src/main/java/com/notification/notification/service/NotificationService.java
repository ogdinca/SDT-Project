package com.notification.notification.service;

import com.notification.notification.service.model.NotificationRequest;
import com.notification.notification.service.observer.NotificationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final List<NotificationObserver> observers;

    // Constructor injection - Spring will automatically inject all NotificationObserver beans
    public NotificationService(List<NotificationObserver> observers) {
        this.observers = observers;
        log.info("NotificationService initialized with {} observers", observers.size());
        observers.forEach(obs -> log.info("  - {} Notifier registered", obs.getType()));
    }

    /**
     * Send notification to all observers
     */
    public void sendNotification(NotificationRequest request) {
        log.info("Received notification request: {}", request);

        String type = request.getType();
        String message = request.getMessage();

        if (type == null || type.equalsIgnoreCase("ALL")) {
            // Send to all observers
            notifyAll(message);
        } else {
            // Send to specific observer type
            notifyByType(message, type);
        }
    }

    private void notifyAll(String message) {
        log.info("Notifying all {} observers", observers.size());
        for (NotificationObserver observer : observers) {
            try {
                observer.update(message);
            } catch (Exception e) {
                log.error("Error notifying {}: {}", observer.getType(), e.getMessage());
            }
        }
    }

    private void notifyByType(String message, String type) {
        log.info("Notifying {} observer", type);

        boolean found = false;
        for (NotificationObserver observer : observers) {
            if (observer.getType().equalsIgnoreCase(type)) {
                try {
                    observer.update(message);
                    found = true;
                } catch (Exception e) {
                    log.error("Error notifying {}: {}", type, e.getMessage());
                }
            }
        }

        if (!found) {
            log.warn("No observer found for type: {}", type);
        }
    }

    public List<String> getAvailableNotificationTypes() {
        return observers.stream()
                .map(NotificationObserver::getType)
                .toList();
    }
}