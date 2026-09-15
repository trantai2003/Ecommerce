package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Store extends BaseEntity {

    @Column(name = "store_image")
    private String storeImage;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "description")
    private String description;
}
