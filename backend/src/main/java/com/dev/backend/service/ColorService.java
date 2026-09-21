package com.dev.backend.service;

import com.dev.backend.dto.request.ColorRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ColorResponse;

import java.util.UUID;

public interface ColorService {
    BaseResponse<ColorResponse> create(UUID currentUserId, ColorRequest request);
}
