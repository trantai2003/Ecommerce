package com.dev.backend.service;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductService {
    Void create_v1(UUID currentUserId, ProductCreateRequest request);

    BaseResponse<ProductResponse> create_v2(UUID currentUserId, ProductCreateRequest request);

    BaseResponse<ProductResponse> create_v3(UUID currentUserId, ProductCreateRequest request);
}
