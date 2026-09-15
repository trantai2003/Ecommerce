package com.dev.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailService;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Trích xuất JWT Token từ Header của Request
            String jwt = getJwtFromRequest(request);

            // 2. Kiểm tra tính hợp lệ của Token
            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                // Lấy email từ token
                String email = jwtTokenProvider.getEmailFromJWT(jwt);

                // Tải thông tin người dùng tương ứng từ Database
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);


                // Tạo đối tượng xác thực (Authentication) của Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu đối tượng xác thực vào Security Context (Đăng nhập thành công cho Request hiện tại)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception ex){
            log.error("Không thể thiết lập xác thực người dùng trong Security Context", ex);
        }
        // Chuyển tiếp Request sang bộ lọc tiếp theo
        filterChain.doFilter(request, response);
    }
}
