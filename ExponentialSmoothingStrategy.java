package com.example.project2;

public class ExponentialSmoothingStrategy implements RestockStrategy {
    @Override
    public int calculateRestock(InventoryItem item) {
        // Example logic: Restock 30% of current quantity
        return (int) (item.getQuantity() * 0.3);
    }
}