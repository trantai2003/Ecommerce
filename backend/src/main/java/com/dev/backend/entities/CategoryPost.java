package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category_posts")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryPost extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;
}
