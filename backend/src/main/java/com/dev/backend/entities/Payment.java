package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity {

    @Column(name = "type")
    private String type;

    @Column(name = "total_price", precision = 15, scale = 2)
    private BigDecimal totalPrice;
}
