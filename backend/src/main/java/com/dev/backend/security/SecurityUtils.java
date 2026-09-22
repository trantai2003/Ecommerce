package com.dev.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Lay thong tin nguoi dang dang nhap tu SecurityContext.
 * JwtAuthenticationFilter da giai ma token va luu CustomUserDetails vao day cho moi request.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /** Tra ve id user dang dang nhap, hoac null neu request khong co token hop le */
    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            return null;   // chua dang nhap (anonymous)
        }
        return user.getId();
    }
}
