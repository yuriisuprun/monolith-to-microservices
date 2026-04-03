package com.suprun.demo.modules.inventory.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "inventory_item")
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    public InventoryItemEntity(Long id, String productName, Integer quantity) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
