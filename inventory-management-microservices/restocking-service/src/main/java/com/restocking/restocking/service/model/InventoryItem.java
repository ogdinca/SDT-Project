package com.restocking.restocking.service.model;

public class InventoryItem {

    private Long itemId;
    private String itemName;
    private Integer quantity;
    private Integer categoryId;

    // Default constructor
    public InventoryItem() {
    }

    // Constructor with all fields
    public InventoryItem(Long itemId, String itemName, Integer quantity, Integer categoryId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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