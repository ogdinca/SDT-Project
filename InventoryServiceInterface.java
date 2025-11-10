package com.example.project2;

import java.util.List;

public interface InventoryServiceInterface {
    String addItem(String name, int quantity, int categoryId);
    List<InventoryItem> listItems();
}