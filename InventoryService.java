// InventoryService.java
package com.example.project2;

import java.util.ArrayList;
import java.util.List;

public class InventoryService implements InventoryServiceInterface {
    private List<InventoryItem> inventoryItems;
    private List<InventoryObserver> Observers;
    private RestockStrategy RestockStrategy;
    private ItemFactory itemFactory = new ItemFactory();


    public InventoryService() {
        this.inventoryItems = new ArrayList<>();
        this.Observers = new ArrayList<>();
    }

    public String addItem(String itemName, int quantity, int categoryId) {
        InventoryItem newItem = itemFactory.createItem(itemName, quantity, categoryId);
        inventoryItems.add(newItem);
        notifyObservers("Item added: " + newItem.getItemName());
        return "Item added successfully";
    }

    public String updateItem(int itemId, int newQuantity) {
        for (InventoryItem item : inventoryItems) {
            if (item.getItemId() == itemId) {
                item.setQuantity(newQuantity);
                return "Item updated successfully";
            }
        }
        return "Item not found";
    }

    public String deleteItem(int itemId) {
        for (InventoryItem item : inventoryItems) {
            if (item.getItemId() == itemId) {
                inventoryItems.remove(item);
                return "Item deleted successfully: " + item.toString();
            }
        }
        return "Item not found";
    }

    public InventoryItem getItem(int itemId) {
        for (InventoryItem item : inventoryItems) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public List<InventoryItem> listItems() {
        return new ArrayList<>(inventoryItems);
    }

    public List<InventoryItem> getItemsByCategory(int categoryId) {
        List<InventoryItem> itemsInCategory = new ArrayList<>();
        for (InventoryItem item : inventoryItems) {
            if (item.getCategoryId() == categoryId) {
                itemsInCategory.add(item);
            }
        }
        return itemsInCategory;
    }

    public void addObserver(InventoryObserver observer) {
        Observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (InventoryObserver observer : Observers) {
            observer.update(message);
        }
    }
}
