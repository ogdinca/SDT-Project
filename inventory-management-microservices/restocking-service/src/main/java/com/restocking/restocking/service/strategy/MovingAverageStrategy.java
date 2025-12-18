package com.restocking.restocking.service.strategy;

import com.restocking.restocking.service.model.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class MovingAverageStrategy implements RestockStrategy {

    @Override
    public int calculateRestock(InventoryItem item) {
        // Moving Average: Restock 20% of current quantity
        // In real scenario, this would analyze historical consumption data
        int currentQuantity = item.getQuantity();

        // Base restock amount (20% of current stock)
        int baseRestock = (int) Math.ceil(currentQuantity * 0.2);

        // Minimum restock amount to ensure we always order something meaningful
        int minimumRestock = 5;

        // Return the larger of the two
        return Math.max(baseRestock, minimumRestock);
    }

    @Override
    public String getStrategyName() {
        return "MOVING_AVERAGE";
    }

    @Override
    public String getDescription() {
        return "Calculates restock quantity based on 20% of current stock levels. " +
                "Suitable for items with stable, predictable consumption patterns.";
    }
}