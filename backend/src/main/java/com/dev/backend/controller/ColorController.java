package com.dev.backend.controller;

import com.dev.backend.dto.request.ColorRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ColorResponse;
import com.dev.backend.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/color")
public class ColorController {


    private final ColorService colorService;

    @PostMapping
    public ResponseEntity<BaseResponse<ColorResponse>> create(@RequestParam UUID currentUserId, @RequestBody ColorRequest request) {
        return ResponseEntity.ok(colorService.create(currentUserId, request));
    }
}
