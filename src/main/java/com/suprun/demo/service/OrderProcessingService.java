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
    public Order createFullOrder(Order order, Payment payment, InventoryItem inventoryItem) {

        Order savedOrder = orderRepository.save(order);

        payment.setOrderId(savedOrder.getId());
        paymentRepository.save(payment);

        InventoryItem item = inventoryRepository.findById(inventoryItem.getId())
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        if (item.getQuantity() < inventoryItem.getQuantity()) {
            throw new RuntimeException("Not enough inventory for product: " + item.getProductName());
        }

        item.setQuantity(item.getQuantity() - inventoryItem.getQuantity());
        inventoryRepository.save(item);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
