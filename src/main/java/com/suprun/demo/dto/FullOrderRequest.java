package com.suprun.demo.dto;

import com.suprun.demo.domain.InventoryItem;
import com.suprun.demo.domain.Order;
import com.suprun.demo.domain.Payment;
import lombok.Data;

@Data
public class FullOrderRequest {

    private Order order;
    private Payment payment;
    private InventoryItem inventoryItem;
}