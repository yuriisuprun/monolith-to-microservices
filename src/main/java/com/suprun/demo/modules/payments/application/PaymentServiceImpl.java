package com.suprun.demo.modules.payments.application;

import com.suprun.demo.modules.payments.api.PaymentService;
import com.suprun.demo.modules.payments.persistence.PaymentEntity;
import com.suprun.demo.modules.payments.persistence.PaymentJpaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentJpaRepository paymentRepo;

    public PaymentServiceImpl(PaymentJpaRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @Override
    @Transactional
    public void recordPayment(long orderId, String status) {
        paymentRepo.save(new PaymentEntity(null, orderId, status));
        log.info("Payment recorded: orderId={} status={}", orderId, status);
    }
}

