package com.suprun.demo.modules.inventory.api;

public interface InventoryService {

    /**
     * Decreases inventory for a product. In the monolith this happens in-process and can participate in a single DB transaction.
     * When migrating to microservices, this call becomes a boundary (likely async with a reservation workflow).
     */
    void reserve(long productId, int quantity);
}

