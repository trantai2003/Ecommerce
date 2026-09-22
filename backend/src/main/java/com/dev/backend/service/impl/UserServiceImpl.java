package com.dev.backend.service.impl;

import com.dev.backend.dto.request.LoginRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.LoginResponse;
import com.dev.backend.dto.response.UserResponse;
import com.dev.backend.dto.response.UserTokenResponse;
import com.dev.backend.entities.Role;
import com.dev.backend.entities.User;
import com.dev.backend.mapper.UserMapper;
import com.dev.backend.repository.RoleRepository;
import com.dev.backend.repository.UserRepository;
import com.dev.backend.security.JwtTokenProvider;
import com.dev.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest loginRequest) {
        BaseResponse<LoginResponse> response = new BaseResponse<>();
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null) {
            response.setMsg("Người dùng không tồn tại");
            response.setCode(400);
            return response;
        }
        ArrayList<String> roles = new ArrayList<>(
                user.getRoles().stream().map(Role::getName).toList()
        );
        boolean checkPassword = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (checkPassword) {
            UserTokenResponse userTokenResponse = new UserTokenResponse();
            userTokenResponse.setId(user.getId());
            userTokenResponse.setEmail(user.getEmail());
            userTokenResponse.setName(user.getName());
            String token = jwtTokenProvider.generateToken(userTokenResponse);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setEmail(user.getEmail());
            loginResponse.setAddress(user.getAddress());
            loginResponse.setName(user.getName());
            loginResponse.setStatus(user.getStatus());
            loginResponse.setRoles(roles);
            response.setData(loginResponse);
            response.setCode(200);
            response.setMsg("Đăng nhập thành công");
            return response;
        } else {
            response.setMsg("Sai tài khoản hoặc mật khẩu. Vui lòng kiểm tra lại");
            response.setCode(400);
            return response;
        }
    }

    @Override
    public Page<UserResponse> getListUser(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        if (search != null && !search.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(search, pageable);
        }
        return users.map(userMapper::userResponse);
    }
}
