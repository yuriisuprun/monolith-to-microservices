package com.suprun.demo.shared.outbox;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OutboxService {

    private final OutboxEventJpaRepository repo;

    public OutboxService(OutboxEventJpaRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void record(String eventType, String aggregateType, String aggregateId, String payload) {
        repo.save(new OutboxEventEntity(
                null,
                eventType,
                aggregateType,
                aggregateId,
                payload,
                Instant.now(),
                null
        ));
    }
}

