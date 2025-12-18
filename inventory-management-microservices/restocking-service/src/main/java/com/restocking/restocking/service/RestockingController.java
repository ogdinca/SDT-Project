package com.restocking.restocking.service;
import com.restocking.restocking.service.model.RestockRecommendation;
import com.restocking.restocking.service.service.RestockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restocking")
public class RestockingController {

    private static final Logger log = LoggerFactory.getLogger(RestockingController.class);

    private final RestockingService restockingService;

    public RestockingController(RestockingService restockingService) {
        this.restockingService = restockingService;
    }

    /**
     * GET /api/restocking/strategies - Get available restocking strategies
     */
    @GetMapping("/strategies")
    public ResponseEntity<List<Map<String, String>>> getStrategies() {
        log.info("Fetching available strategies");
        List<Map<String, String>> strategies = restockingService.getAvailableStrategies();
        return ResponseEntity.ok(strategies);
    }

    /**
     * GET /api/restocking/calculate/{itemId} - Calculate restock for specific item
     * Query param: strategy (default: MOVING_AVERAGE)
     */
    @GetMapping("/calculate/{itemId}")
    public ResponseEntity<RestockRecommendation> calculateRestock(
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "MOVING_AVERAGE") String strategy) {

        log.info("Calculating restock for item {} with strategy {}", itemId, strategy);

        try {
            RestockRecommendation recommendation = restockingService.calculateRestockForItem(itemId, strategy);
            return ResponseEntity.ok(recommendation);
        } catch (Exception e) {
            log.error("Error calculating restock: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate restock: " + e.getMessage());
        }
    }

    /**
     * GET /api/restocking/analyze - Analyze all items and generate recommendations
     * Query param: strategy (default: MOVING_AVERAGE)
     */
    @GetMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeInventory(
            @RequestParam(defaultValue = "MOVING_AVERAGE") String strategy) {

        log.info("Analyzing inventory with strategy: {}", strategy);

        try {
            List<RestockRecommendation> recommendations = restockingService.analyzeAllItems(strategy);

            Map<String, Object> response = new HashMap<>();
            response.put("strategy", strategy);
            response.put("totalRecommendations", recommendations.size());
            response.put("recommendations", recommendations);

            long urgentCount = recommendations.stream().filter(RestockRecommendation::isUrgent).count();
            response.put("urgentCount", urgentCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error analyzing inventory: {}", e.getMessage());
            throw new RuntimeException("Failed to analyze inventory: " + e.getMessage());
        }
    }

    /**
     * GET /api/restocking/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "restocking-service");
        return ResponseEntity.ok(health);
    }
}