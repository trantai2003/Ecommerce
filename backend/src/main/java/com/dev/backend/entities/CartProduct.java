package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cart_products",
        uniqueConstraints = @UniqueConstraint(name = "uk_cart_user_variant",
                columnNames = {"user_id", "product_variant_id"}))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CartProduct extends BaseEntity {

    // Khach chon cu the mau + size -> tro toi bien the, san pham lay qua productVariant.getProduct()
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Builder.Default
    @Column(name = "quantity")
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
