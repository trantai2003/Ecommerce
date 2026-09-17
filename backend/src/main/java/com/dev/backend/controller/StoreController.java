package com.dev.backend.controller;

import com.dev.backend.dto.request.StoreRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.StoreResponse;
import com.dev.backend.service.StoreService;
import lombok.RequiredArgsConstructor;
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
}

