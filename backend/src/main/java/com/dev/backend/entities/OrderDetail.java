package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class OrderDetail extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // Giu product de con lich su don khi bien the bi xoa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    // Gia tai thoi diem dat hang (copy tu productVariant.price)
    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;
}
