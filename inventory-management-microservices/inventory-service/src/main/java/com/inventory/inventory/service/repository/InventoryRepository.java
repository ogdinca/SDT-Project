package com.inventory.inventory.service.repository;

import com.inventory.inventory.service.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    // Custom query method - Spring Data JPA will implement this automatically
    List<InventoryItem> findByCategoryId(Integer categoryId);

    // Optional: Find by name (useful for searching)
    List<InventoryItem> findByItemNameContainingIgnoreCase(String itemName);
}