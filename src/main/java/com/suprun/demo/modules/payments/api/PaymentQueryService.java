package com.suprun.demo.modules.payments.api;

import java.util.Optional;

public interface PaymentQueryService {

    Optional<String> findStatusForOrder(long orderId);
}

