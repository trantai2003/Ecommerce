package com.dev.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "store_followers",
        uniqueConstraints = @UniqueConstraint(name = "uk_follower_user_store", columnNames = {"user_id", "store_id"}))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StoreFollower extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
