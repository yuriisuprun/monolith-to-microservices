package com.suprun.demo.modules.orders.api;

import java.math.BigDecimal;

public record OrderResponse(
        long id,
        String customerName,
        BigDecimal totalAmount,
        String paymentStatus
) {
}

