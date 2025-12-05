package com.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${restocking.service.url}")
    private String restockingServiceUrl;

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ==================== INVENTORY SERVICE ROUTES ====================

    @GetMapping("/inventory")
    public ResponseEntity<?> getAllInventory() {
        log.info("Gateway: Routing to Inventory Service - GET all items");
        return forwardRequest(inventoryServiceUrl + "/api/inventory", HttpMethod.GET, null);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<?> getInventoryItem(@PathVariable Long id) {
        log.info("Gateway: Routing to Inventory Service - GET item {}", id);
        return forwardRequest(inventoryServiceUrl + "/api/inventory/" + id, HttpMethod.GET, null);
    }

    @PostMapping("/inventory")
    public ResponseEntity<?> createInventoryItem(@RequestBody Map<String, Object> body) {
        log.info("Gateway: Routing to Inventory Service - POST create item");
        return forwardRequest(inventoryServiceUrl + "/api/inventory", HttpMethod.POST, body);
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<?> updateInventoryItem(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        log.info("Gateway: Routing to Inventory Service - PUT update item {}", id);
        return forwardRequest(inventoryServiceUrl + "/api/inventory/" + id, HttpMethod.PUT, body);
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable Long id) {
        log.info("Gateway: Routing to Inventory Service - DELETE item {}", id);
        return forwardRequest(inventoryServiceUrl + "/api/inventory/" + id, HttpMethod.DELETE, null);
    }

    @GetMapping("/inventory/category/{categoryId}")
    public ResponseEntity<?> getInventoryByCategory(@PathVariable Integer categoryId) {
        log.info("Gateway: Routing to Inventory Service - GET items by category {}", categoryId);
        return forwardRequest(inventoryServiceUrl + "/api/inventory/category/" + categoryId, HttpMethod.GET, null);
    }

    // ==================== NOTIFICATION SERVICE ROUTES ====================

    @PostMapping("/notifications/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> body) {
        log.info("Gateway: Routing to Notification Service - POST send notification");
        return forwardRequest(notificationServiceUrl + "/api/notifications/send", HttpMethod.POST, body);
    }

    @GetMapping("/notifications/types")
    public ResponseEntity<?> getNotificationTypes() {
        log.info("Gateway: Routing to Notification Service - GET notification types");
        return forwardRequest(notificationServiceUrl + "/api/notifications/types", HttpMethod.GET, null);
    }

    @GetMapping("/notifications/health")
    public ResponseEntity<?> notificationHealth() {
        log.info("Gateway: Routing to Notification Service - GET health");
        return forwardRequest(notificationServiceUrl + "/api/notifications/health", HttpMethod.GET, null);
    }

    // ==================== RESTOCKING SERVICE ROUTES ====================

    @GetMapping("/restocking/strategies")
    public ResponseEntity<?> getRestockingStrategies() {
        log.info("Gateway: Routing to Restocking Service - GET strategies");
        return forwardRequest(restockingServiceUrl + "/api/restocking/strategies", HttpMethod.GET, null);
    }

    @GetMapping("/restocking/calculate/{itemId}")
    public ResponseEntity<?> calculateRestock(@PathVariable Long itemId,
                                              @RequestParam(required = false) String strategy) {
        log.info("Gateway: Routing to Restocking Service - GET calculate restock for item {}", itemId);
        String url = restockingServiceUrl + "/api/restocking/calculate/" + itemId;
        if (strategy != null) {
            url += "?strategy=" + strategy;
        }
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @GetMapping("/restocking/analyze")
    public ResponseEntity<?> analyzeInventory(@RequestParam(required = false) String strategy) {
        log.info("Gateway: Routing to Restocking Service - GET analyze inventory");
        String url = restockingServiceUrl + "/api/restocking/analyze";
        if (strategy != null) {
            url += "?strategy=" + strategy;
        }
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @GetMapping("/restocking/health")
    public ResponseEntity<?> restockingHealth() {
        log.info("Gateway: Routing to Restocking Service - GET health");
        return forwardRequest(restockingServiceUrl + "/api/restocking/health", HttpMethod.GET, null);
    }

    // ==================== GATEWAY SPECIFIC ROUTES ====================

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> gatewayHealth() {
        log.info("Gateway: Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("gateway", "UP");

        // Check all services
        Map<String, String> services = new HashMap<>();
        services.put("inventory", checkServiceHealth(inventoryServiceUrl + "/api/inventory"));
        services.put("notification", checkServiceHealth(notificationServiceUrl + "/api/notifications/health"));
        services.put("restocking", checkServiceHealth(restockingServiceUrl + "/api/restocking/health"));

        health.put("services", services);

        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> gatewayInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Inventory Management API Gateway");
        info.put("version", "1.0.0");
        info.put("description", "Single entry point for all microservices");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("inventory", inventoryServiceUrl);
        endpoints.put("notification", notificationServiceUrl);
        endpoints.put("restocking", restockingServiceUrl);

        info.put("services", endpoints);

        return ResponseEntity.ok(info);
    }

    // ==================== HELPER METHODS ====================

    private ResponseEntity<?> forwardRequest(String url, HttpMethod method, Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    String.class
            );

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());

        } catch (Exception e) {
            log.error("Error forwarding request to {}: {}", url, e.getMessage());

            Map<String, String> error = new HashMap<>();
            error.put("error", "Service unavailable");
            error.put("message", e.getMessage());
            error.put("targetUrl", url);

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(error);
        }
    }

    private String checkServiceHealth(String url) {
        try {
            restTemplate.getForEntity(url, String.class);
            return "UP";
        } catch (Exception e) {
            return "DOWN";
        }
    }
}