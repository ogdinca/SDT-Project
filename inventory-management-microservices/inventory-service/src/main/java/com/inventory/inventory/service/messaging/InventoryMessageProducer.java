package com.inventory.inventory.service.messaging;

import com.inventory.inventory.service.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryMessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public InventoryMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishItemCreated(InventoryEvent event) {
        try {
            log.info("Publishing ITEM_CREATED event to RabbitMQ: {}", event);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INVENTORY_EXCHANGE,
                    RabbitMQConfig.INVENTORY_CREATED_KEY,
                    event
            );
            log.info("Successfully published ITEM_CREATED event for item: {}", event.getItemId());
        } catch (Exception e) {
            log.error("Failed to publish ITEM_CREATED event: {}", e.getMessage());
            // Even if messaging fails, the main operation succeeded
            // This is a KEY BENEFIT: fault tolerance
        }
    }

    public void publishItemUpdated(InventoryEvent event) {
        try {
            log.info("Publishing ITEM_UPDATED event to RabbitMQ: {}", event);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INVENTORY_EXCHANGE,
                    RabbitMQConfig.INVENTORY_UPDATED_KEY,
                    event
            );
            log.info("Successfully published ITEM_UPDATED event for item: {}", event.getItemId());
        } catch (Exception e) {
            log.error("Failed to publish ITEM_UPDATED event: {}", e.getMessage());
        }
    }

    public void publishLowStockAlert(InventoryEvent event) {
        try {
            log.info("Publishing LOW_STOCK alert to RabbitMQ: {}", event);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INVENTORY_EXCHANGE,
                    RabbitMQConfig.INVENTORY_LOW_STOCK_KEY,
                    event
            );
            log.info("Successfully published LOW_STOCK alert for item: {}", event.getItemId());
        } catch (Exception e) {
            log.error("Failed to publish LOW_STOCK alert: {}", e.getMessage());
        }
    }
}