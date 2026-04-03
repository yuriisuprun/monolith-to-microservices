package com.suprun.demo.modules.inventory.application;

import com.suprun.demo.modules.inventory.api.InsufficientInventoryException;
import com.suprun.demo.modules.inventory.api.InventoryItemNotFoundException;
import com.suprun.demo.modules.inventory.api.InventoryService;
import com.suprun.demo.modules.inventory.persistence.InventoryItemEntity;
import com.suprun.demo.modules.inventory.persistence.InventoryItemJpaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemJpaRepository inventoryRepo;

    public InventoryServiceImpl(InventoryItemJpaRepository inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    @Override
    @Transactional
    public void reserve(long productId, int quantity) {
        InventoryItemEntity item = inventoryRepo.findById(productId)
                .orElseThrow(() -> new InventoryItemNotFoundException(productId));

        int available = item.getQuantity() == null ? 0 : item.getQuantity();
        if (available < quantity) {
            throw new InsufficientInventoryException(item.getProductName(), available, quantity);
        }

        item.setQuantity(available - quantity);
        inventoryRepo.save(item);
        log.info("Inventory reserved: productId={} productName={} reserved={} remaining={}",
                item.getId(), item.getProductName(), quantity, item.getQuantity());
    }
}

