package com.suprun.demo.modules.inventory.api;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(String productName, int available, int requested) {
        super("Insufficient inventory for product '" + productName + "' (available=" + available + ", requested=" + requested + ")");
    }
}

