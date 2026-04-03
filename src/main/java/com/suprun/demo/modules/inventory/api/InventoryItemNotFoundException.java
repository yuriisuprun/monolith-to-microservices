package com.suprun.demo.modules.inventory.api;

public class InventoryItemNotFoundException extends RuntimeException {

    public InventoryItemNotFoundException(long productId) {
        super("Inventory item not found for id: " + productId);
    }
}

