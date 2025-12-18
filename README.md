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

## **Running the System Using Docker**
It is very easy to use Docker to run all the services together.
#### **Step 1: Navigate to Project Directory**

```bash
cd inventory-management-microservices
```

#### **Step 2: Build and Start All Services**

```bash
docker-compose up --build
```

This command will:
- Build Docker images for all services
- Start all containers in the correct order
- Display logs from all services

#### **Step 3: Wait for Services to Start**

Watch the logs. The system is ready when you see:

```
eureka-server        | Started EurekaServerApplication
inventory-service    | Started InventoryServiceApplication
notification-service | Started NotificationServiceApplication
restocking-service   | Started RestockingServiceApplication
api-gateway          | Started ApiGatewayApplication
```

This typically takes 2-3 minutes for the first run.

#### **Step 4: Verify All Services Are Running**

```bash
docker-compose ps
```

Expected output: All services should show `Up` status.
