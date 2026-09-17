package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** Danh muc mau dung chung cho moi san pham */
@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Color extends BaseEntity {

    @Column(name = "color_name", unique = true)
    private String colorName;

    @Column(name = "hex_code", length = 7)
    private String hexCode;
}
