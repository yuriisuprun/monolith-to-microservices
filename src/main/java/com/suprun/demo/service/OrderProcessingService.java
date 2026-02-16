package com.suprun.demo.service;

import com.suprun.demo.domain.InventoryItem;
import com.suprun.demo.domain.Order;
import com.suprun.demo.domain.Payment;
import com.suprun.demo.exception.InsufficientInventoryException;
import com.suprun.demo.exception.ResourceNotFoundException;
import com.suprun.demo.repository.InventoryRepository;
import com.suprun.demo.repository.OrderRepository;
import com.suprun.demo.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingService {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingService.class);

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
        log.debug("Creating order: productId={}, quantity={}, customer={}",
                productId, quantity, order != null ? order.getCustomerName() : "<unknown>");

        InventoryItem inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Inventory item not found for id: {}", productId);
                    return new ResourceNotFoundException("Inventory item not found for id: " + productId);
                });

        if (inventory.getQuantity() < quantity) {
            log.warn("Insufficient inventory for product: {} (available: {}, requested: {})",
                    inventory.getProductName(), inventory.getQuantity(), quantity);
            throw new InsufficientInventoryException("Insufficient inventory for product: " + inventory.getProductName());
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id={}", savedOrder.getId());

        payment.setOrderId(savedOrder.getId());
        paymentRepository.save(payment);
        log.debug("Payment saved for order id={}", savedOrder.getId());

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        log.info("Inventory updated for product id={} newQuantity={}", inventory.getId(), inventory.getQuantity());

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        log.debug("Retrieving all orders");
        return orderRepository.findAll();
    }
}