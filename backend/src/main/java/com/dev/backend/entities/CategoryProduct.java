package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category_products")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryProduct extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryProduct parent;

    // 1 danh muc cha - N danh muc con (tu tham chieu qua parent_id)
    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<CategoryProduct> children = new ArrayList<>();

    // 1 danh muc - N san pham. FK nam o products.category_product_id
    // mappedBy = ten field "categoryProduct" trong entity Product
    // KHONG cascade: xoa danh muc khong duoc xoa theo san pham (DB dang ON DELETE SET NULL)
    @Builder.Default
    @OneToMany(mappedBy = "categoryProduct")
    private List<Product> products = new ArrayList<>();
}
