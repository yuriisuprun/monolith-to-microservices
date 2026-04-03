package com.suprun.demo.shared.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, Long> {

    @Query("select e from OutboxEventEntity e where e.publishedAt is null order by e.id asc")
    List<OutboxEventEntity> findUnpublished(Pageable pageable);
}

