package com.dev.backend.security;

import com.dev.backend.dto.response.UserTokenResponse;
import com.dev.backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationInMs;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 1. Tao JWT Token tu Email cua nguoi dung
    public String generateToken(UserTokenResponse userTokenResponse) {
        return generateToken(userTokenResponse, List.of());
    }

    // 1b. Tao JWT Token kem danh sach quyen (ROLE_ADMIN, ROLE_USER, ...)
    public String generateToken(UserTokenResponse userTokenResponse, Collection<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(userTokenResponse.getEmail())
                // Map.of khong nhan gia tri null -> dung String.valueOf va gia tri mac dinh
                .claim("id", String.valueOf(userTokenResponse.getId()))
                .claim("name", userTokenResponse.getName() == null ? "" : userTokenResponse.getName())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    // 2. Lay Email tu JWT Token da duoc xac thuc
    public String getEmailFromJWT(String token) {
        return parseClaims(token).getSubject();
    }

    // 3. Xac thuc JWT Token (kiem tra han dung, chu ky, dinh dang)
    public boolean validateToken(String authToken) {
        try {
            parseClaims(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("JWT Token co chu ky khong hop le!");
        } catch (MalformedJwtException e) {
            log.error("JWT Token khong dung dinh dang!");
        } catch (ExpiredJwtException e) {
            log.error("JWT Token da het han!");
        } catch (UnsupportedJwtException e) {
            log.error("JWT Token khong duoc ho tro!");
        } catch (IllegalArgumentException e) {
            log.error("JWT Token bi trong!");
        } catch (JwtException e) {
            log.error("JWT Token khong hop le: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
