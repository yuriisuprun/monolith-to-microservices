package com.suprun.demo.modules.orders.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    protected OrderEntity() {
    }

    public OrderEntity(Long id, String customerName, BigDecimal totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
    }

}
