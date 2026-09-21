package com.dev.backend.repository;

import com.dev.backend.entities.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColorRepository extends JpaRepository<Color, UUID> {
    boolean existsByColorNameIgnoreCase(String colorName);
}
