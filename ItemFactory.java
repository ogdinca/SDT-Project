package com.example.project2;

public class ItemFactory {
    public InventoryItem createItem(String name, int quantity, int categoryId) {
        return new InventoryItem(name, quantity, categoryId);
    }
}
