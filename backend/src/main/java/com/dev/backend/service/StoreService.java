package com.dev.backend.service;

import com.dev.backend.dto.request.StoreRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.StoreResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface StoreService {

    BaseResponse<StoreResponse> create(UUID currentUserId, StoreRequest request);

    Page<StoreResponse> getListStore(int page, int size, String search);

    BaseResponse<StoreResponse> update(UUID storeId, StoreRequest request);

    BaseResponse<Void> delete(UUID storeId, UUID currentId);
}
