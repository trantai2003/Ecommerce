package com.dev.backend.controller;

import com.dev.backend.dto.request.LoginRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.LoginResponse;
import com.dev.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse<LoginResponse> response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
