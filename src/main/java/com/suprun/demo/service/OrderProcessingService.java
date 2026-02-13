package com.suprun.demo.service;

import com.suprun.demo.domain.InventoryItem;
import com.suprun.demo.domain.Order;
import com.suprun.demo.domain.Payment;
import com.suprun.demo.repository.InventoryRepository;
import com.suprun.demo.repository.OrderRepository;
import com.suprun.demo.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;

    public OrderProcessingService(OrderRepository orderRepository,
                                  PaymentRepository paymentRepository,
                                  InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Order createOrder(Order order, Payment payment, Long productId, Integer quantity) {

        InventoryItem inventory = inventoryRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory item not found for id: " + productId));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient inventory for product: " + inventory.getProductName());
        }

        Order savedOrder = orderRepository.save(order);

        payment.setOrderId(savedOrder.getId());
        paymentRepository.save(payment);

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}