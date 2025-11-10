package com.example.project2;

import java.util.ArrayList;
import java.util.List;

public class OfflineInventoryDecorator implements InventoryServiceInterface {
    private InventoryServiceInterface wrappedService;
    private List<String> localCache = new ArrayList<>();
    private boolean isOffline = true;

    public OfflineInventoryDecorator(InventoryServiceInterface service) {
        this.wrappedService = service;
    }

    @Override
    public String addItem(String name, int quantity, int categoryId) {
        if (isOffline) {
            String action = "ADD:" + name + "," + quantity + "," + categoryId;
            localCache.add(action);
            return "Action cached for offline use";
        }
        return wrappedService.addItem(name, quantity, categoryId);
    }

    @Override
    public List<InventoryItem> listItems() {
        if (isOffline) {
            return new ArrayList<>();
        }
        return wrappedService.listItems();
    }

    public void syncWhenOnline() {
        for (String action : localCache) {
            System.out.println("Syncing: " + action);
        }
        localCache.clear();
    }
}
