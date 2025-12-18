# **Offline-First Inventory Management System for Collaborative Activities**  
**Team Members**: Dinca Andrei-Ioan, Chiar Andrei-Cristian

---

## **Project Description**  
This project is an **offline-first inventory management system** designed for **collaborative activities** such as university labs, workshops, community projects, etc. It addresses the need to track consumables, tools, and reservations while supporting offline operations and predictive restocking. Key features include:  

### **Functionalities**  
1. **User Authentication**  
   - Secure login with roles (Admin, Member, Guest).  
   - Role-specific permissions (e.g., Admins can delete items; Members can only checkout).  

2. **Inventory CRUD operations**  
   - CRUD operations for items (consumables/tools) with metadata:  
     - Category, unit of measure, location, expiry date, batch/lot.  
   - Manual adjustments with reason codes and photo evidence.  

3. **Offline-First Workflows**  
   - Mobile/web client for benchside scanning and operations without connectivity. 

4. **Predictive Restocking**  
   - Auto-generate purchase orders when stock hits reorder thresholds.  
   - Forecast restocking quantities using moving-average or exponential smoothing.  

5. **Billing & Reservations**  
   - Credit deduction for consumable usage.  
   - Time-based reservations for equipment.  
   - Refunds, cancellations, and dispute resolution with evidence.  

6. **Reporting & Analytics**  
   - Real-time stock levels and consumption trends.

---



## **Design Patterns**  
We will implement **four design patterns** to address key challenges in the system:  

---

### **1. Strategy Pattern**  
**Purpose**: Dynamically switch between authentication/permission strategies and restocking algorithms.  
**How It Solves a Problem**:  
- **Authentication**: Use different strategies for user login.  
- **Restocking**: Implement strategies for predictive forecasts.  
**Advantages Over Simpler Approaches**:  
- Avoids hardcoding logic in the core system.  
- Easy to add new strategies without modifying existing code.  

---

### **2. Observer Pattern**  
**Purpose**: Notify users of inventory changes and billing updates.  
**How It Solves a Problem**:  
- When an item is checked out or stock is low, observers are triggered.  
**Advantages Over Simpler Approaches**:  
- Decouples inventory logic from notification logic.  
- Supports multiple notification channels (email, SMS) without code copy paste.  

---

### **3. Decorator Pattern**  
**Purpose**: Add offline-first capabilities and security features dynamically.  
**How It Solves a Problem**:  
- Wrap the `InventoryService` with a `LocalCacheDecorator` to store operations locally when offline.  
- Use a decorator to encrypt sensitive data (e.g., user emails).  
**Advantages Over Simpler Approaches**:  
- Adds functionality without modifying the core `InventoryService`.  
- Supports combinations of behaviors (e.g., encryption + caching).  

---

### **4. Factory Method Pattern**  
**Purpose**: Centralize the creation of complex objects like an inventory item or the reservations.  
**How It Solves a Problem**:  
- Use a factory to create items with default tags or batch-specific metadata.  
- Use a factory to generate reservations with time-based constraints.  
**Advantages Over Simpler Approaches**:  
- Ensures consistent object creation.  
- Easier to extend for new item types.

---

## **Message Queue Integration**

We use **RabbitMQ** as our message broker to enable asynchronous, decoupled communication between services. This is a critical architectural decision that provides: There are many reasons as to why we would use this such as scalability, fault tolerence and decoupling.

### **Architecture Components**

#### **Exchanges**
- **inventory.exchange** (Topic Exchange)
  - Routes messages based on routing keys
  - Flexible pattern matching for different event types

#### **Queues**
1. **inventory.events.queue**
   - Receives: CREATED, UPDATED events
   - TTL: 24 hours
   - Purpose: General inventory change notifications

2. **notification.queue**
   - Receives: LOW_STOCK alerts
   - TTL: 1 hour
   - Purpose: Urgent notifications requiring immediate attention

#### **Routing Keys**
- `inventory.created` → New items added
- `inventory.updated` → Item quantities changed
- `inventory.lowstock` → Items below threshold

### **Message Flow Example**

```
1. User creates item with quantity 5 (below threshold of 10)
   
2. Inventory Service:
   ├─ Saves to database ✓
   ├─ Publishes "inventory.created" event to RabbitMQ ✓
   └─ Publishes "inventory.lowstock" alert to RabbitMQ ✓
   
3. RabbitMQ:
   ├─ Routes "created" event to inventory.events.queue
   └─ Routes "lowstock" alert to notification.queue
   
4. Notification Service:
   ├─ Consumer 1: Processes created event (CONSOLE notification)
   └─ Consumer 2: Processes lowstock alert (ALL channels notification)
```

### **Configuration**

#### **RabbitMQ Connection** (application.properties)
```properties
spring.rabbitmq.host=rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin123
```

#### **Message Producer** (Inventory Service)
```java
@Service
public class InventoryMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    
    public void publishItemCreated(InventoryEvent event) {
        rabbitTemplate.convertAndSend(
            INVENTORY_EXCHANGE,
            INVENTORY_CREATED_KEY,
            event
        );
    }
}
```

#### **Message Consumer** (Notification Service)
```java
@Service
public class NotificationMessageConsumer {
    
    @RabbitListener(queues = "inventory.events.queue")
    public void handleInventoryEvent(InventoryEvent event) {
        // Process event asynchronously
    }
    
    @RabbitListener(queues = "notification.queue")
    public void handleLowStockAlert(InventoryEvent event) {
        // Handle urgent alerts
    }
}
```

---

## **CI/CD Pipeline**

We use **GitHub Actions** to implement a comprehensive CI/CD pipeline that automatically builds, tests, and deploys our microservices on every code push.

The pipeline is split into stages:
   - Build and Test
   - Docker Build
   - Integration Tests
