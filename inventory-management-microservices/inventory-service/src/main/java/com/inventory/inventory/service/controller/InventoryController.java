package com.inventory.inventory.service.controller;

import com.inventory.inventory.service.model.InventoryItem;
import com.inventory.inventory.service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Manual constructor for dependency injection
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * POST /api/inventory - Add a new item
     */
    @PostMapping
    public ResponseEntity<InventoryItem> addItem(@RequestBody CreateItemRequest request) {
        InventoryItem item = inventoryService.addItem(
                request.getItemName(),
                request.getQuantity(),
                request.getCategoryId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    /**
     * GET /api/inventory - Get all items
     */
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllItems() {
        List<InventoryItem> items = inventoryService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * GET /api/inventory/{id} - Get item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getItem(@PathVariable Long id) {
        InventoryItem item = inventoryService.getItem(id);
        return ResponseEntity.ok(item);
    }

    /**
     * GET /api/inventory/category/{categoryId} - Get items by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<InventoryItem>> getItemsByCategory(@PathVariable Integer categoryId) {
        List<InventoryItem> items = inventoryService.getItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }

    /**
     * PUT /api/inventory/{id} - Update item quantity
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateItem(
            @PathVariable Long id,
            @RequestBody UpdateItemRequest request) {
        InventoryItem item = inventoryService.updateItem(id, request.getNewQuantity());
        return ResponseEntity.ok(item);
    }

    /**
     * DELETE /api/inventory/{id} - Delete an item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // Request DTOs (Data Transfer Objects)
    static class CreateItemRequest {
        private String itemName;
        private Integer quantity;
        private Integer categoryId;

        // Default constructor
        public CreateItemRequest() {}

        // Constructor with all fields
        public CreateItemRequest(String itemName, Integer quantity, Integer categoryId) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.categoryId = categoryId;
        }

        // Getters and Setters
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
    }

    static class UpdateItemRequest {
        private Integer newQuantity;

        // Default constructor
        public UpdateItemRequest() {}

        // Constructor
        public UpdateItemRequest(Integer newQuantity) {
            this.newQuantity = newQuantity;
        }

        // Getter and Setter
        public Integer getNewQuantity() {
            return newQuantity;
        }

        public void setNewQuantity(Integer newQuantity) {
            this.newQuantity = newQuantity;
        }
    }
}