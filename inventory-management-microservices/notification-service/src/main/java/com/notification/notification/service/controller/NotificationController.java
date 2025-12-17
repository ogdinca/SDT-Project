package com.notification.notification.service.controller;

import com.notification.notification.service.model.NotificationRequest;
import com.notification.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    // Constructor injection
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * POST /api/notifications/send - Send a notification
     * This endpoint is called by other microservices (e.g., Inventory Service)
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody NotificationRequest request) {
        log.info("Received notification request from external service");

        try {
            notificationService.sendNotification(request);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Notification sent successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage());

            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send notification: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getNotificationTypes() {
        List<String> types = notificationService.getAvailableNotificationTypes();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "notification-service");
        return ResponseEntity.ok(health);
    }
}