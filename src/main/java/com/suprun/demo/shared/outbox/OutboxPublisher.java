package com.suprun.demo.shared.outbox;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

@Slf4j
public class OutboxPublisher {

    private final OutboxEventJpaRepository repo;
    private final OutboxProperties props;

    public OutboxPublisher(OutboxEventJpaRepository repo, OutboxProperties props) {
        this.repo = repo;
        this.props = props;
    }

    /**
     * This is intentionally "log-only" in the monolith example.
     * When moving to microservices, this is typically replaced by publishing to Kafka/Rabbit/etc.
     */
    @Scheduled(fixedDelayString = "PT2S")
    @Transactional
    public void publishBatch() {
        List<OutboxEventEntity> events = repo.findUnpublished(PageRequest.of(0, props.batchSize()));
        for (OutboxEventEntity e : events) {
            log.info("OUTBOX publish (noop): id={} type={} aggregate={}({}) payload={}",
                    e.getId(), e.getEventType(), e.getAggregateType(), e.getAggregateId(), e.getPayload());
            e.markPublished(Instant.now());
        }
    }
}

