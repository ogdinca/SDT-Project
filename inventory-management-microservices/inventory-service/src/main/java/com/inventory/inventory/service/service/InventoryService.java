package com.inventory.inventory.service.service;

import com.inventory.inventory.service.model.InventoryItem;
import com.inventory.inventory.service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;
    private final ItemFactory itemFactory;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    // Manual constructor for dependency injection
    public InventoryService(InventoryRepository inventoryRepository,
                            RestTemplate restTemplate,
                            ItemFactory itemFactory) {
        this.inventoryRepository = inventoryRepository;
        this.restTemplate = restTemplate;
        this.itemFactory = itemFactory;
    }

    /**
     * Add a new item to inventory
     * Uses Factory pattern to create items
     */
    public InventoryItem addItem(String itemName, Integer quantity, Integer categoryId) {
        // Use factory to create item (Factory Method Pattern)
        InventoryItem newItem = itemFactory.createItem(itemName, quantity, categoryId);

        // Save to database
        InventoryItem savedItem = inventoryRepository.save(newItem);

        // Notify other services (inter-service communication)
        notifyItemAdded(savedItem);

        log.info("Item added successfully: {}", savedItem);
        return savedItem;
    }

    /**
     * Update item quantity
     */
    public InventoryItem updateItem(Long itemId, Integer newQuantity) {
        Optional<InventoryItem> itemOptional = inventoryRepository.findById(itemId);

        if (itemOptional.isPresent()) {
            InventoryItem item = itemOptional.get();
            item.setQuantity(newQuantity);
            InventoryItem updatedItem = inventoryRepository.save(item);

            log.info("Item updated successfully: {}", updatedItem);
            return updatedItem;
        } else {
            log.warn("Item not found with ID: {}", itemId);
            throw new RuntimeException("Item not found with ID: " + itemId);
        }
    }

    /**
     * Delete an item
     */
    public void deleteItem(Long itemId) {
        if (inventoryRepository.existsById(itemId)) {
            inventoryRepository.deleteById(itemId);
            log.info("Item deleted successfully with ID: {}", itemId);
        } else {
            log.warn("Item not found with ID: {}", itemId);
            throw new RuntimeException("Item not found with ID: " + itemId);
        }
    }

    /**
     * Get a single item by ID
     */
    public InventoryItem getItem(Long itemId) {
        return inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemId));
    }

    /**
     * Get all items
     */
    public List<InventoryItem> getAllItems() {
        return inventoryRepository.findAll();
    }

    /**
     * Get items by category
     */
    public List<InventoryItem> getItemsByCategory(Integer categoryId) {
        return inventoryRepository.findByCategoryId(categoryId);
    }

    /**
     * Inter-service communication: Notify the Notification Service
     * This replaces the Observer pattern with REST API calls
     */
    private void notifyItemAdded(InventoryItem item) {
        try {
            String message = "Item added: " + item.getItemName() +
                    " (Quantity: " + item.getQuantity() + ")";

            // Create notification request
            NotificationRequest request = new NotificationRequest(message);

            // Call Notification Service via REST
            String url = notificationServiceUrl + "/api/notifications/send";
            restTemplate.postForObject(url, request, String.class);

            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            // Don't fail the entire operation if notification fails
        }
    }

    // Inner class for notification requests
    static class NotificationRequest {
        private String message;

        // Constructor
        public NotificationRequest(String message) {
            this.message = message;
        }

        // Getter and Setter
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}