// InventoryItem.java
package com.example.project2;

import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    private static int nextItemId = 1; // Counter for generating unique item IDs

    private int itemId;
    private String itemName;
    private int quantity;
    private int categoryId;

    // Constructors
    public InventoryItem(String itemName, int quantity, int categoryId) {
        this.itemId = nextItemId++;
        this.itemName = itemName;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    // Getter and Setter methods
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", categoryId=" + categoryId +
                '}';
    }
}
