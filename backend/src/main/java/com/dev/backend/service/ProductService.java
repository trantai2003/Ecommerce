package com.dev.backend.service;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductService {
    BaseResponse<ProductResponse> create(UUID currentUserId, ProductCreateRequest request);
}
