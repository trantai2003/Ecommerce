package com.dev.backend.service;

import com.dev.backend.dto.request.LoginRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.LoginResponse;
import com.dev.backend.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    BaseResponse<LoginResponse> login(LoginRequest loginRequest);

    Page<UserResponse>getListUser(int page, int size, String search);
}
