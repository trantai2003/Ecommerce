package com.dev.backend.security;

import com.dev.backend.entities.Role;
import com.dev.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UUID id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    // Factory method chuyen doi tu User Entity sang CustomUserDetails de Spring Security su dung
    public static CustomUserDetails build(User user) {
        // Anh xa danh sach Role cua User sang SimpleGrantedAuthority ("ROLE_" + name)
        List<GrantedAuthority> authorities = user.getRoles() == null || user.getRoles().isEmpty()
                ? List.of(new SimpleGrantedAuthority(ROLE_PREFIX + "USER"))
                : user.getRoles().stream()
                        .map(Role::getName)
                        .map(name -> name.startsWith(ROLE_PREFIX) ? name : ROLE_PREFIX + name)
                        .map(SimpleGrantedAuthority::new)
                        .map(GrantedAuthority.class::cast)
                        .toList();

        boolean enabled = Boolean.TRUE.equals(user.getStatus()) && !Boolean.TRUE.equals(user.getIsDelete());

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                enabled,
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Su dung email lam username de xac thuc dang nhap
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled; // status = true va is_delete = false
    }
}
