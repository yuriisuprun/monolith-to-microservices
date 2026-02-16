package com.suprun.demo.controller;

import com.suprun.demo.domain.Order;
import com.suprun.demo.dto.FullOrderRequest;
import com.suprun.demo.service.OrderProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderProcessingService service;

    public OrderController(OrderProcessingService service) {
        this.service = service;
    }

    @PostMapping
    public Order createOrder(@RequestBody FullOrderRequest request) {
        log.info("Received create order request: productId={}, quantity={}, customer={}",
                request.getProductId(), request.getQuantity(),
                request.getOrder() != null ? request.getOrder().getCustomerName() : "<unknown>");
        Order result = service.createOrder(
                request.getOrder(),
                request.getPayment(),
                request.getProductId(),
                request.getQuantity()
        );
        log.info("Order created with id={}", result.getId());
        return result;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return service.getAllOrders();
    }
}
