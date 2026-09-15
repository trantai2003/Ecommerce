package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Role extends BaseEntity {

    /** ADMIN, SELLER, USER — khi cấp quyền Spring Security dùng "ROLE_" + name */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;
}