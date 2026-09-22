package com.dev.backend.controller;

import com.dev.backend.dto.request.SizeRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.SizeResponse;
import com.dev.backend.entities.Size;
import com.dev.backend.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/size")
public class SizeController {
    private final SizeService sizeService;

    @PostMapping
    public ResponseEntity<BaseResponse<SizeResponse>> create(@RequestParam UUID currentUserId, @RequestBody SizeRequest request) {
        return ResponseEntity.ok(sizeService.create(currentUserId, request));
    }
}
