package com.notification.notification.service.messaging;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InventoryEvent implements Serializable {

    private Long itemId;
    private String itemName;
    private Integer quantity;
    private Integer categoryId;
    private String eventType;
    private LocalDateTime timestamp;
    private String message;

    public InventoryEvent() {
    }

    public InventoryEvent(Long itemId, String itemName, Integer quantity,
                          Integer categoryId, String eventType, String message) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "InventoryEvent{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", categoryId=" + categoryId +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}