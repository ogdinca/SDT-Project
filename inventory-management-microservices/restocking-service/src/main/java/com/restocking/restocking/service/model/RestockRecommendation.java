package com.restocking.restocking.service.model;

public class RestockRecommendation {

    private Long itemId;
    private String itemName;
    private Integer currentQuantity;
    private Integer recommendedQuantity;
    private String strategy;
    private String reason;
    private boolean urgent;

    // Default constructor
    public RestockRecommendation() {
    }

    // Constructor with all fields
    public RestockRecommendation(Long itemId, String itemName, Integer currentQuantity,
                                 Integer recommendedQuantity, String strategy,
                                 String reason, boolean urgent) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.currentQuantity = currentQuantity;
        this.recommendedQuantity = recommendedQuantity;
        this.strategy = strategy;
        this.reason = reason;
        this.urgent = urgent;
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

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Integer getRecommendedQuantity() {
        return recommendedQuantity;
    }

    public void setRecommendedQuantity(Integer recommendedQuantity) {
        this.recommendedQuantity = recommendedQuantity;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    @Override
    public String toString() {
        return "RestockRecommendation{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", currentQuantity=" + currentQuantity +
                ", recommendedQuantity=" + recommendedQuantity +
                ", strategy='" + strategy + '\'' +
                ", reason='" + reason + '\'' +
                ", urgent=" + urgent +
                '}';
    }
}