package com.dev.backend.service;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.request.ProductUpdateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.dto.response.ProductUpdateResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductService {
    Void create_v1(UUID currentUserId, ProductCreateRequest request);

    BaseResponse<ProductResponse> create_v2(UUID currentUserId, ProductCreateRequest request);

    BaseResponse<ProductResponse> create_v3(UUID currentUserId, ProductCreateRequest request);

    BaseResponse<ProductUpdateResponse> update_v1(UUID productId, ProductUpdateRequest updateRequest);

    BaseResponse<ProductUpdateResponse> update_v2(UUID productId, ProductUpdateRequest updateRequest);
}
