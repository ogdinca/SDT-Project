// Main.java
package com.example.project2;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventoryService inventoryService = new InventoryService();
        Scanner scanner = new Scanner(System.in);
        inventoryService.addObserver(new EmailNotifier());
        inventoryService.addObserver(new ConsoleNotifier());

        int choice;
        do {
            System.out.println("Select an option:");
            System.out.println("1. Add Item");
            System.out.println("2. Update Item");
            System.out.println("3. Delete Item");
            System.out.println("4. List Items by Category ID");
            System.out.println("5. List All Items");
            System.out.println("0. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice){
                case 1:
                    System.out.println("Enter Item Name:");
                    String itemName = scanner.nextLine();

                    System.out.println("Enter Quantity:");
                    int quantity = scanner.nextInt();

                    System.out.println("Enter Category ID:");
                    int categoryId = scanner.nextInt();

                    System.out.println(inventoryService.addItem(itemName, quantity, categoryId));
                    break;

                case 2:
                    System.out.println("Enter Item ID to Update:");
                    int updateItemId = scanner.nextInt();

                    System.out.println("Enter New Quantity:");
                    int newQuantity = scanner.nextInt();

                    System.out.println(inventoryService.updateItem(updateItemId, newQuantity));
                    break;

                case 3:
                    System.out.println("Enter Item ID to Delete:");
                    int deleteItemId = scanner.nextInt();

                    System.out.println(inventoryService.deleteItem(deleteItemId));
                    break;


                case 4:
                    System.out.println("Enter Category ID to List Items:");
                    int categoryIdToSearch = scanner.nextInt();
                    List<InventoryItem> itemsByCategory = inventoryService.getItemsByCategory(categoryIdToSearch);
                    System.out.println("Items with Category ID '" + categoryIdToSearch + "':");
                    displayItems(itemsByCategory);
                    break;

                case 5:
                    List<InventoryItem> allItems = inventoryService.listItems();
                    System.out.println("All Items:");
                    displayItems(allItems);
                    break;

                case 0:
                    System.out.println("Exiting Program.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
           }while (choice != 0);
        }


    private static void displayItems(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            System.out.println(item);
        }
        System.out.println("--------");
    }
}
