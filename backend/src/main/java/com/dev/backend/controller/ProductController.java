package com.dev.backend.controller;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.request.ProductUpdateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.dto.response.ProductUpdateResponse;
import com.dev.backend.dto.response.ProductVariantUpdateResponse;
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

    @PutMapping("/update_v1/{id}")
    public ResponseEntity<BaseResponse<ProductUpdateResponse>> update_v1(@PathVariable UUID id,
                                                                         @RequestBody ProductUpdateRequest updateRequest) {
        return ResponseEntity.ok(productService.update_v1(id, updateRequest));
    }

    @PutMapping("/update_v2/{id}")
    public ResponseEntity<BaseResponse<ProductUpdateResponse>> update_v2(@PathVariable UUID id,
                                                                         @RequestBody ProductUpdateRequest updateRequest) {
        return ResponseEntity.ok(productService.update_v2(id, updateRequest));
    }



}
