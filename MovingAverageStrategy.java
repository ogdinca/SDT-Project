package com.example.project2;

public class MovingAverageStrategy implements RestockStrategy {
    @Override
    public int calculateRestock(InventoryItem item) {
        return (int) (item.getQuantity() * 0.2); //20%
    }
}