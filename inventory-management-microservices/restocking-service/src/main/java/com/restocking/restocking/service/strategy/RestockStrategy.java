package com.restocking.restocking.service.strategy;

import com.restocking.restocking.service.model.InventoryItem;

public interface RestockStrategy {

    /**
     * Calculate restock quantity for an item
     * @param item The inventory item
     * @return Recommended restock quantity
     */
    int calculateRestock(InventoryItem item);

    /**
     * Get the name of this strategy
     * @return Strategy name
     */
    String getStrategyName();

    /**
     * Get description of how this strategy works
     * @return Strategy description
     */
    String getDescription();
}