package com.suprun.demo.modules.payments.api;

public interface PaymentService {

    void recordPayment(long orderId, String status);
}

