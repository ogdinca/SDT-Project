package com.inventory.inventory.service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer categoryId;

    // Default constructor (required by JPA)
    public InventoryItem() {
    }

    // Constructor without ID (for creating new items)
    public InventoryItem(String itemName, Integer quantity, Integer categoryId) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.categoryId = categoryId;
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

    // toString for debugging
    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", categoryId=" + categoryId +
                '}';
    }

    // equals and hashCode for JPA entity comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return itemId != null && itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}