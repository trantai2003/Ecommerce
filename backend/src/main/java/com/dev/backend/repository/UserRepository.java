package com.dev.backend.repository;

import com.dev.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    //  select top 1 * from [USER] where [name] = 'thientm'
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    //      select * from [USER] where [name] = 'thientm'

    Page<User> findByNameContainingIgnoreCase(String username, Pageable pageable);

    Optional<User> findById(UUID id);
}