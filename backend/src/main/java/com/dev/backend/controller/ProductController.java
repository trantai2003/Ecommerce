package com.dev.backend.controller;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ResponseEntity<BaseResponse<ProductResponse>> create(@RequestParam UUID currentUserId, @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create(currentUserId, request));
    }
}
