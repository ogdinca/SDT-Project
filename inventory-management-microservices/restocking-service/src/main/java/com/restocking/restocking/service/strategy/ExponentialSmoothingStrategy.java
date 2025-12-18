package com.restocking.restocking.service.strategy;

import com.restocking.restocking.service.model.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class ExponentialSmoothingStrategy implements RestockStrategy {

    // Smoothing factor (alpha) - higher values give more weight to recent data
    private static final double ALPHA = 0.3;

    @Override
    public int calculateRestock(InventoryItem item) {
        // Exponential Smoothing: Restock 30% of current quantity
        // This strategy gives more weight to recent consumption patterns
        int currentQuantity = item.getQuantity();

        // Calculate restock with smoothing factor
        int smoothedRestock = (int) Math.ceil(currentQuantity * ALPHA);

        // Minimum restock amount
        int minimumRestock = 8;

        // Return the larger of the two
        return Math.max(smoothedRestock, minimumRestock);
    }

    @Override
    public String getStrategyName() {
        return "EXPONENTIAL_SMOOTHING";
    }

    @Override
    public String getDescription() {
        return "Uses exponential smoothing with alpha=0.3 to calculate restock quantity. " +
                "Gives more weight to recent consumption trends. " +
                "Suitable for items with changing demand patterns.";
    }
}