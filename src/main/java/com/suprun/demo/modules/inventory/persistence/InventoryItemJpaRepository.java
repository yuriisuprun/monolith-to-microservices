package com.suprun.demo.modules.inventory.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemJpaRepository extends JpaRepository<InventoryItemEntity, Long> {
}

