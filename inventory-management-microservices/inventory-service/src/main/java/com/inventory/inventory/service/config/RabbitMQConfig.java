package com.inventory.inventory.service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String INVENTORY_EVENTS_QUEUE = "inventory.events.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    // Exchange names
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";

    // Routing keys
    public static final String INVENTORY_CREATED_KEY = "inventory.created";
    public static final String INVENTORY_UPDATED_KEY = "inventory.updated";
    public static final String INVENTORY_LOW_STOCK_KEY = "inventory.lowstock";

    /**
     * Inventory Events Queue - for general inventory events
     */
    @Bean
    public Queue inventoryEventsQueue() {
        return QueueBuilder.durable(INVENTORY_EVENTS_QUEUE)
                .withArgument("x-message-ttl", 86400000) // 24 hours TTL
                .build();
    }

    /**
     * Notification Queue - for sending notifications
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-message-ttl", 3600000) // 1 hour TTL
                .build();
    }

    /**
     * Topic Exchange for routing messages based on routing keys
     */
    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    /**
     * Bind inventory events queue to exchange with created routing key
     */
    @Bean
    public Binding inventoryCreatedBinding() {
        return BindingBuilder
                .bind(inventoryEventsQueue())
                .to(inventoryExchange())
                .with(INVENTORY_CREATED_KEY);
    }

    /**
     * Bind inventory events queue to exchange with updated routing key
     */
    @Bean
    public Binding inventoryUpdatedBinding() {
        return BindingBuilder
                .bind(inventoryEventsQueue())
                .to(inventoryExchange())
                .with(INVENTORY_UPDATED_KEY);
    }

    /**
     * Bind notification queue to exchange with low stock routing key
     */
    @Bean
    public Binding lowStockNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(inventoryExchange())
                .with(INVENTORY_LOW_STOCK_KEY);
    }

    /**
     * Message converter for JSON serialization
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate with JSON converter
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}