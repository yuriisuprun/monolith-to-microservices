package com.suprun.demo.modules.orders;

import com.suprun.demo.modules.inventory.persistence.InventoryItemJpaRepository;
import com.suprun.demo.modules.orders.api.CreateOrderRequest;
import com.suprun.demo.modules.orders.persistence.OrderJpaRepository;
import com.suprun.demo.modules.payments.persistence.PaymentJpaRepository;
import com.suprun.demo.shared.outbox.OutboxEventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class OrderApiIntegrationTest {

    @Autowired WebApplicationContext ctx;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = webAppContextSetup(ctx).build();
    }

    @Autowired InventoryItemJpaRepository inventoryRepo;
    @Autowired OrderJpaRepository orderRepo;
    @Autowired PaymentJpaRepository paymentRepo;
    @Autowired OutboxEventJpaRepository outboxRepo;

    @Test
    void createOrder_createsOrderPayment_updatesInventory_andWritesOutbox() throws Exception {
        long productId = 1L;
        int beforeQty = inventoryRepo.findById(productId).orElseThrow().getQuantity();

        CreateOrderRequest req = new CreateOrderRequest(
                "Alice",
                new BigDecimal("123.45"),
                productId,
                2,
                "PAID"
        );

        String body = """
                {"customerName":"Alice","totalAmount":123.45,"productId":1,"quantity":2,"paymentStatus":"PAID"}
                """;

        mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.paymentStatus").value("PAID"));

        assertThat(orderRepo.count()).isEqualTo(1);
        assertThat(paymentRepo.count()).isEqualTo(1);
        assertThat(outboxRepo.count()).isEqualTo(1);

        int afterQty = inventoryRepo.findById(productId).orElseThrow().getQuantity();
        assertThat(afterQty).isEqualTo(beforeQty - 2);
    }

    @Test
    void createOrder_withInsufficientInventory_returns400ProblemDetail() throws Exception {
        CreateOrderRequest req = new CreateOrderRequest(
                "Bob",
                new BigDecimal("10.00"),
                1L,
                10_000,
                "PAID"
        );

        String body = """
                {"customerName":"Bob","totalAmount":10.00,"productId":1,"quantity":10000,"paymentStatus":"PAID"}
                """;

        mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Insufficient Inventory"));
    }
}
