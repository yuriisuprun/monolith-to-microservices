package com.suprun.demo.modules.orders.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotBlank String customerName,
        @NotNull @Positive BigDecimal totalAmount,
        @NotNull Long productId,
        @NotNull @Positive Integer quantity,
        @NotBlank String paymentStatus
) {
}

