package com.suprun.demo.controller;

import com.suprun.demo.domain.Order;
import com.suprun.demo.dto.FullOrderRequest;
import com.suprun.demo.service.OrderProcessingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProcessingService service;

    public OrderController(OrderProcessingService service) {
        this.service = service;
    }

    @PostMapping
    public Order createOrder(@RequestBody FullOrderRequest request) {
        return service.createOrder(
                request.getOrder(),
                request.getPayment(),
                request.getProductId(),
                request.getQuantity()
        );
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }
}
