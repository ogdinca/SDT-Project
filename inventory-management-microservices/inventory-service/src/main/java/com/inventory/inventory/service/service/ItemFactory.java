package com.inventory.inventory.service.service;

import com.inventory.inventory.service.model.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory {

    public InventoryItem createItem(String name, Integer quantity, Integer categoryId) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        return new InventoryItem(name, quantity, categoryId);
    }

    public InventoryItem createConsumableItem(String name, Integer quantity) {
        return new InventoryItem(name, quantity, 1);
    }

    public InventoryItem createToolItem(String name, Integer quantity) {
        return new InventoryItem(name, quantity, 2);
    }
}