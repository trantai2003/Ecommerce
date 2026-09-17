package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** Danh muc size dung chung cho moi san pham */
@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Size extends BaseEntity {

    @Column(name = "size_name", unique = true)
    private String sizeName;

    // Thu tu hien thi: S < M < L < XL
    @Builder.Default
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
