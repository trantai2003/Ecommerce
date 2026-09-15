package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Size extends BaseEntity {

    @Column(name = "size_name")
    private String sizeName;

    @Builder.Default
    @Column(name = "stock")
    private Integer stock = 0;
}
