package com.notification.notification.service.messaging;

import com.notification.notification.service.NotificationService;
import com.notification.notification.service.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
@Service
public class NotificationMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationMessageConsumer.class);

    private final NotificationService notificationService;

    public NotificationMessageConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "inventory.events.queue")
    public void handleInventoryEvent(InventoryEvent event) {
        try {
            log.info("Received inventory event from RabbitMQ: {}", event);

            String message = String.format(
                    "Inventory %s: Item '%s' (ID: %d) - Quantity: %d",
                    event.getEventType(),
                    event.getItemName(),
                    event.getItemId(),
                    event.getQuantity()
            );

            NotificationRequest request = new NotificationRequest(message, "CONSOLE");
            notificationService.sendNotification(request);

            log.info("Successfully processed inventory event: {}", event.getEventType());

        } catch (Exception e) {
            log.error("Error processing inventory event: {}", e.getMessage());
            throw e;
        }
    }

    @RabbitListener(queues = "notification.queue")
    public void handleLowStockAlert(InventoryEvent event) {
        try {
            log.warn("Received LOW STOCK alert from RabbitMQ: {}", event);

            String message = String.format(
                    "%s - Current Stock: %d units. Please restock immediately!",
                    event.getMessage(),
                    event.getQuantity()
            );

            NotificationRequest request = new NotificationRequest(message, "ALL");
            notificationService.sendNotification(request);

            log.info("Successfully processed low stock alert for item: {}", event.getItemId());

        } catch (Exception e) {
            log.error("Error processing low stock alert: {}", e.getMessage());
            throw e;
        }
    }
}