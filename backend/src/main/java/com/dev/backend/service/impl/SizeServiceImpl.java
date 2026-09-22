package com.dev.backend.service.impl;

import com.dev.backend.dto.request.SizeRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.SizeResponse;
import com.dev.backend.entities.Size;
import com.dev.backend.mapper.SizeMapper;
import com.dev.backend.repository.SizeRepository;
import com.dev.backend.repository.UserRepository;
import com.dev.backend.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SizeServiceImpl implements SizeService {
    private final UserRepository userRepository;
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public SizeServiceImpl(UserRepository userRepository, SizeRepository sizeRepository, SizeMapper sizeMapper) {
        this.userRepository = userRepository;
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }

    @Override
    public BaseResponse<SizeResponse> create(UUID currentUserId, SizeRequest request) {
        BaseResponse<SizeResponse> response = new BaseResponse<>();
        if (!userRepository.existsById(currentUserId)) {
            response.setCode(404);
            response.setMsg("Người dùng không tồn tại");
        }
        if (sizeRepository.existsBySizeNameContainingIgnoreCase(request.getSizeName())) {
            response.setCode(404);
            response.setMsg("Size đã tồn tại");
        }
        Size size = new Size();
        size.setSizeName(request.getSizeName());
        size.setSortOrder(request.getSortOrderSize());
        sizeRepository.save(size);

        response.setMsg("Tạo size thành công");
        response.setCode(200);
        response.setData(sizeMapper.sizeResponse(size));
        return response;
    }
}
