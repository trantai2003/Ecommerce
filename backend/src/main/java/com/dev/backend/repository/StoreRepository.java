package com.dev.backend.repository;

import com.dev.backend.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    boolean existsByStoreNameIgnoreCase(String name);

    // Trung ten voi cua hang KHAC (loai tru chinh no) - dung khi update
    boolean existsByStoreNameIgnoreCaseAndIdNot(String storeName, UUID id);

    boolean existsById(UUID id);

    Page<Store> findByStoreNameContainingIgnoreCase(String storeName, Pageable pageable);

}
