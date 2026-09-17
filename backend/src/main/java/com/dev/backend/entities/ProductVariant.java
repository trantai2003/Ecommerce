package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Gia + ton kho cua 1 to hop san pham + mau + size */
@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(name = "uk_variant_product_color_size",
                columnNames = {"product_id", "color_id", "size_id"}))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @Column(name = "sku", unique = true, length = 100)
    private String sku;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Builder.Default
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
