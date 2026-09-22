package com.dev.backend.service;

import com.dev.backend.dto.request.SizeRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.SizeResponse;
import com.dev.backend.entities.Size;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SizeService {
    BaseResponse<SizeResponse> create(UUID currentUserId, SizeRequest request);
}
