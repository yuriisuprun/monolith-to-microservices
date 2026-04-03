package com.suprun.demo.modules.orders.application;

import com.suprun.demo.modules.inventory.api.InventoryService;
import com.suprun.demo.modules.orders.api.CreateOrderRequest;
import com.suprun.demo.modules.orders.api.OrderResponse;
import com.suprun.demo.modules.orders.api.OrderService;
import com.suprun.demo.modules.orders.persistence.OrderEntity;
import com.suprun.demo.modules.orders.persistence.OrderJpaRepository;
import com.suprun.demo.modules.payments.api.PaymentQueryService;
import com.suprun.demo.modules.payments.api.PaymentService;
import com.suprun.demo.shared.outbox.OutboxService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final InventoryService inventory;
    private final PaymentService payments;
    private final PaymentQueryService paymentQueries;
    private final OrderJpaRepository orders;
    private final OutboxService outbox;
    private final JsonMapper json;

    public OrderServiceImpl(
            InventoryService inventory,
            PaymentService payments,
            PaymentQueryService paymentQueries,
            OrderJpaRepository orders,
            OutboxService outbox,
            JsonMapper json
    ) {
        this.inventory = inventory;
        this.payments = payments;
        this.paymentQueries = paymentQueries;
        this.orders = orders;
        this.outbox = outbox;
        this.json = json;
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        inventory.reserve(request.productId(), request.quantity());

        OrderEntity saved = orders.save(new OrderEntity(null, request.customerName(), request.totalAmount()));
        payments.recordPayment(saved.getId(), request.paymentStatus());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("orderId", saved.getId());
        payload.put("customerName", saved.getCustomerName());
        payload.put("totalAmount", saved.getTotalAmount());
        payload.put("productId", request.productId());
        payload.put("quantity", request.quantity());
        payload.put("paymentStatus", request.paymentStatus());

        outbox.record(
                "OrderCreated",
                "Order",
                String.valueOf(saved.getId()),
                toJson(payload)
        );

        log.info("Order created: id={}", saved.getId());
        return new OrderResponse(saved.getId(), saved.getCustomerName(), saved.getTotalAmount(), request.paymentStatus());
    }

    @Override
    public List<OrderResponse> list() {
        return orders.findAll().stream()
                .map(o -> new OrderResponse(
                        o.getId(),
                        o.getCustomerName(),
                        o.getTotalAmount(),
                        paymentQueries.findStatusForOrder(o.getId()).orElse("UNKNOWN")
                ))
                .toList();
    }

    private String toJson(Map<String, Object> payload) {
        try {
            // LinkedHashMap helps keep output stable for tests/logs.
            return json.writeValueAsString(new LinkedHashMap<>(payload));
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Failed to serialize outbox payload", e);
        }
    }
}
