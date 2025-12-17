package com.restocking.restocking.service.service;

import com.restocking.restocking.service.model.InventoryItem;
import com.restocking.restocking.service.model.RestockRecommendation;
import com.restocking.restocking.service.strategy.RestockStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestockingService {

    private static final Logger log = LoggerFactory.getLogger(RestockingService.class);

    private final RestTemplate restTemplate;
    private final Map<String, RestockStrategy> strategies;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${restocking.low-stock-threshold}")
    private int lowStockThreshold;

    @Value("${restocking.critical-stock-threshold}")
    private int criticalStockThreshold;

    // Constructor - Spring auto-injects all RestockStrategy beans
    public RestockingService(RestTemplate restTemplate, List<RestockStrategy> strategyList) {
        this.restTemplate = restTemplate;
        this.strategies = new HashMap<>();

        // Register all strategies by name
        for (RestockStrategy strategy : strategyList) {
            strategies.put(strategy.getStrategyName(), strategy);
            log.info("Registered strategy: {}", strategy.getStrategyName());
        }
    }

    /**
     * Calculate restock recommendation for a specific item using a strategy
     */
    public RestockRecommendation calculateRestockForItem(Long itemId, String strategyName) {
        log.info("Calculating restock for item {} using strategy {}", itemId, strategyName);

        // Fetch item from Inventory Service
        InventoryItem item = fetchInventoryItem(itemId);

        // Get the strategy
        RestockStrategy strategy = strategies.get(strategyName.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        }

        // Calculate restock quantity
        int recommendedQuantity = strategy.calculateRestock(item);

        // Determine urgency and reason
        boolean urgent = item.getQuantity() <= criticalStockThreshold;
        String reason = determineReason(item.getQuantity());

        // Build recommendation
        RestockRecommendation recommendation = new RestockRecommendation(
                item.getItemId(),
                item.getItemName(),
                item.getQuantity(),
                recommendedQuantity,
                strategy.getStrategyName(),
                reason,
                urgent
        );

        log.info("Recommendation: {}", recommendation);
        return recommendation;
    }

    /**
     * Analyze all inventory items and generate restock recommendations
     */
    public List<RestockRecommendation> analyzeAllItems(String strategyName) {
        log.info("Analyzing all items with strategy: {}", strategyName);

        // Fetch all items from Inventory Service
        List<InventoryItem> allItems = fetchAllInventoryItems();

        List<RestockRecommendation> recommendations = new ArrayList<>();

        for (InventoryItem item : allItems) {
            // Only recommend restocking for low stock items
            if (item.getQuantity() <= lowStockThreshold) {
                try {
                    RestockRecommendation recommendation = calculateRestockForItem(
                            item.getItemId(),
                            strategyName
                    );
                    recommendations.add(recommendation);

                    // Send notification for urgent items
                    if (recommendation.isUrgent()) {
                        sendUrgentNotification(recommendation);
                    }

                } catch (Exception e) {
                    log.error("Error calculating restock for item {}: {}",
                            item.getItemId(), e.getMessage());
                }
            }
        }

        log.info("Generated {} restock recommendations", recommendations.size());
        return recommendations;
    }

    /**
     * Get all available strategies
     */
    public List<Map<String, String>> getAvailableStrategies() {
        List<Map<String, String>> strategyInfo = new ArrayList<>();

        for (RestockStrategy strategy : strategies.values()) {
            Map<String, String> info = new HashMap<>();
            info.put("name", strategy.getStrategyName());
            info.put("description", strategy.getDescription());
            strategyInfo.add(info);
        }

        return strategyInfo;
    }

    /**
     * Fetch a single item from Inventory Service
     */
    private InventoryItem fetchInventoryItem(Long itemId) {
        String url = inventoryServiceUrl + "/api/inventory/" + itemId;
        log.info("Fetching item from: {}", url);

        try {
            ResponseEntity<InventoryItem> response = restTemplate.getForEntity(url, InventoryItem.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch item {}: {}", itemId, e.getMessage());
            throw new RuntimeException("Failed to fetch inventory item: " + e.getMessage());
        }
    }

    /**
     * Fetch all items from Inventory Service
     */
    private List<InventoryItem> fetchAllInventoryItems() {
        String url = inventoryServiceUrl + "/api/inventory";
        log.info("Fetching all items from: {}", url);

        try {
            ResponseEntity<List<InventoryItem>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InventoryItem>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch inventory items: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch inventory items: " + e.getMessage());
        }
    }

    /**
     * Send urgent notification via Notification Service
     */
    private void sendUrgentNotification(RestockRecommendation recommendation) {
        String message = String.format(
                "URGENT: Item '%s' (ID: %d) is critically low! Current: %d, Recommended restock: %d",
                recommendation.getItemName(),
                recommendation.getItemId(),
                recommendation.getCurrentQuantity(),
                recommendation.getRecommendedQuantity()
        );

        try {
            Map<String, String> notificationRequest = new HashMap<>();
            notificationRequest.put("message", message);
            notificationRequest.put("type", "ALL");

            String url = notificationServiceUrl + "/api/notifications/send";
            restTemplate.postForObject(url, notificationRequest, String.class);

            log.info("Urgent notification sent for item {}", recommendation.getItemId());
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
        }
    }

    /**
     * Determine reason for restocking
     */
    private String determineReason(int currentQuantity) {
        if (currentQuantity <= criticalStockThreshold) {
            return "CRITICAL: Stock critically low";
        } else if (currentQuantity <= lowStockThreshold) {
            return "WARNING: Stock below threshold";
        } else {
            return "Preventive restocking";
        }
    }
}