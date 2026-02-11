package com.suprun.demo.controller;

import com.suprun.demo.domain.Order;
import com.suprun.demo.service.OrderProcessingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProcessingService service;

    public OrderController(OrderProcessingService service) {
        this.service = service;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return service.createOrder(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAllOrders();
    }
}
