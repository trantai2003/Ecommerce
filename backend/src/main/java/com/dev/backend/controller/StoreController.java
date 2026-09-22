package com.dev.backend.controller;

import com.dev.backend.dto.request.StoreRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.StoreResponse;
import com.dev.backend.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;


    @PostMapping
    public ResponseEntity<BaseResponse<StoreResponse>> createStore(@RequestParam UUID currentUserId, @RequestBody StoreRequest request) {
        return ResponseEntity.ok(storeService.create(currentUserId, request));
    }

    @GetMapping("getlist")
    public ResponseEntity<Page<StoreResponse>> getListStore(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(storeService.getListStore(page, size, search));
    }

    // Nguoi dang sua duoc lay tu JWT ngay trong service (SecurityUtils.getCurrentUserId())
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<StoreResponse>> updateStore(@PathVariable("id") UUID storeId,
                                                                   @RequestBody StoreRequest request) {
        return ResponseEntity.ok(storeService.update(storeId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteStore(@PathVariable("id") UUID storeId, @RequestParam UUID currentUserId) {
        return ResponseEntity.ok(storeService.delete(storeId, currentUserId));
    }
}

