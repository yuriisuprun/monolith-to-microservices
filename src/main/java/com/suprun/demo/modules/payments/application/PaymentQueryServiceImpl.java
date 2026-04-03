package com.suprun.demo.modules.payments.application;

import com.suprun.demo.modules.payments.api.PaymentQueryService;
import com.suprun.demo.modules.payments.persistence.PaymentJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentJpaRepository repo;

    public PaymentQueryServiceImpl(PaymentJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<String> findStatusForOrder(long orderId) {
        return repo.findFirstByOrderId(orderId).map(p -> p.getStatus());
    }
}

