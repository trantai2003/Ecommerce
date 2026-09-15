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

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<CategoryProduct> children = new ArrayList<>();
}
