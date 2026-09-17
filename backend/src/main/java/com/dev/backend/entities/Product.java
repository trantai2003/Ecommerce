package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_product_id")
    private CategoryProduct categoryProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 1 san pham - N bien the (mau + size + gia + ton kho)
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    private String createdBy;

    // Giu dong bo 2 chieu: dung ham nay thay vi getVariants().add(...)
    public void addVariant(ProductVariant variant) {
        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {
        variants.remove(variant);
        variant.setProduct(null);
    }
}
