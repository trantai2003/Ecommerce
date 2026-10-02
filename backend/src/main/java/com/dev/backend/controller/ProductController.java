package com.dev.backend.controller;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("create_v1")
    public ResponseEntity create_v1(@RequestParam UUID currentUserId, @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create_v1(currentUserId, request));
    }

    @PostMapping("create_v2")
    public ResponseEntity<BaseResponse<ProductResponse>> create_v2(@RequestParam UUID currentUserId, @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create_v2(currentUserId, request));
    }

    @PostMapping("create_v3")
    public ResponseEntity<BaseResponse<ProductResponse>> create_v3(@RequestParam UUID currentUserId, @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create_v3(currentUserId, request));
    }

}
