package com.inventory.inventory.service.service;

import com.inventory.inventory.service.messaging.InventoryEvent;
import com.inventory.inventory.service.messaging.InventoryMessageProducer;
import com.inventory.inventory.service.model.InventoryItem;
import com.inventory.inventory.service.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;
    private final ItemFactory itemFactory;
    private final InventoryMessageProducer messageProducer;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${inventory.low-stock-threshold:10}")
    private int lowStockThreshold;

    public InventoryService(InventoryRepository inventoryRepository,
                            RestTemplate restTemplate,
                            ItemFactory itemFactory,
                            InventoryMessageProducer messageProducer) {
        this.inventoryRepository = inventoryRepository;
        this.restTemplate = restTemplate;
        this.itemFactory = itemFactory;
        this.messageProducer = messageProducer;
    }

    /**
     * Add a new item to inventory
     * NOW WITH ASYNCHRONOUS EVENT PUBLISHING via RabbitMQ
     */
    public InventoryItem addItem(String itemName, Integer quantity, Integer categoryId) {
        // Create item using Factory pattern
        InventoryItem newItem = itemFactory.createItem(itemName, quantity, categoryId);

        // Save to database
        InventoryItem savedItem = inventoryRepository.save(newItem);

        // ASYNC: Publish event to RabbitMQ (NON-BLOCKING)
        InventoryEvent event = new InventoryEvent(
                savedItem.getItemId(),
                savedItem.getItemName(),
                savedItem.getQuantity(),
                savedItem.getCategoryId(),
                "CREATED",
                "New item added to inventory"
        );
        messageProducer.publishItemCreated(event);

        // Check if low stock and send alert
        if (quantity <= lowStockThreshold) {
            InventoryEvent lowStockEvent = new InventoryEvent(
                    savedItem.getItemId(),
                    savedItem.getItemName(),
                    savedItem.getQuantity(),
                    savedItem.getCategoryId(),
                    "LOW_STOCK",
                    String.format("Item '%s' created with low stock: %d", itemName, quantity)
            );
            messageProducer.publishLowStockAlert(lowStockEvent);
        }

        log.info("Item added successfully: {}", savedItem);
        return savedItem;
    }

    /**
     * Update item quantity
     * NOW WITH ASYNCHRONOUS EVENT PUBLISHING via RabbitMQ
     */
    public InventoryItem updateItem(Long itemId, Integer newQuantity) {
        Optional<InventoryItem> itemOptional = inventoryRepository.findById(itemId);

        if (itemOptional.isPresent()) {
            InventoryItem item = itemOptional.get();
            Integer oldQuantity = item.getQuantity();
            item.setQuantity(newQuantity);
            InventoryItem updatedItem = inventoryRepository.save(item);

            // ASYNC: Publish update event to RabbitMQ
            InventoryEvent event = new InventoryEvent(
                    updatedItem.getItemId(),
                    updatedItem.getItemName(),
                    updatedItem.getQuantity(),
                    updatedItem.getCategoryId(),
                    "UPDATED",
                    String.format("Quantity updated from %d to %d", oldQuantity, newQuantity)
            );
            messageProducer.publishItemUpdated(event);

            // Check if now low stock
            if (newQuantity <= lowStockThreshold && oldQuantity > lowStockThreshold) {
                InventoryEvent lowStockEvent = new InventoryEvent(
                        updatedItem.getItemId(),
                        updatedItem.getItemName(),
                        updatedItem.getQuantity(),
                        updatedItem.getCategoryId(),
                        "LOW_STOCK",
                        String.format("Item '%s' is now low in stock: %d", item.getItemName(), newQuantity)
                );
                messageProducer.publishLowStockAlert(lowStockEvent);
            }

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
}