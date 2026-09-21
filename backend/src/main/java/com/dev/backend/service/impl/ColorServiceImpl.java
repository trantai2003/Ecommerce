package com.dev.backend.service.impl;

import com.dev.backend.dto.request.ColorRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ColorResponse;
import com.dev.backend.entities.Color;
import com.dev.backend.mapper.ColorMapper;
import com.dev.backend.repository.ColorRepository;
import com.dev.backend.repository.UserRepository;
import com.dev.backend.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final UserRepository userRepository;
    private final ColorMapper colorMapper;


    public ColorServiceImpl(ColorRepository colorRepository, UserRepository userRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.userRepository = userRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    public BaseResponse<ColorResponse> create(UUID currentUserId, ColorRequest request) {
        BaseResponse<ColorResponse> response = new BaseResponse<>();
        if (!userRepository.existsById(currentUserId)) {
            response.setCode(404);
            response.setMsg("Người dùng không tồn tại");
        }

        String colorName = request.getColorName() == null ? "" : request.getColorName().trim();
        if (colorName.isEmpty()) {
            response.setCode(400);
            response.setMsg("Tên màu không được để trống");
            return response;
        }

        if (colorRepository.existsByColorNameIgnoreCase(colorName)) {
            response.setCode(409);
            response.setMsg("Màu đã tồn tại");
            return response;
        }
        Color color = colorMapper.toColor(request);
        color.setColorName(request.getColorName());
        color.setHexCode(request.getHexCode());
        colorRepository.save(color);

        response.setCode(200);
        response.setMsg("Tạo màu thành công");
        response.setData(colorMapper.toResponse(color));
        return response;

    }
}
