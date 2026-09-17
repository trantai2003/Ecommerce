package com.dev.backend.config;

import com.dev.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 1. Khai bao Bean ma hoa mat khau bang thuat toan BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Khai bao Bean AuthenticationManager de quan ly xac thuc dang nhap
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 3. Cau hinh bao mat chinh (SecurityFilterChain)
    //    Luu y: server.servlet.context-path = /api nen cac matcher ben duoi KHONG co tien to /api
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Vo hieu hoa CSRF vi REST API dung JWT (khong dung Cookie/Session truyen thong)
                .csrf(AbstractHttpConfigurer::disable)

                // Khong luu tru trang thai phien lam viec (STATELESS Session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Tra ve 401 thay vi 403 khi chua dang nhap / token khong hop le
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                // Phan quyen cho cac Endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phep moi nguoi truy cap cac API Authentication (Dang ky, Dang nhap)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/store/**").permitAll()

                        // Trang HTML tinh trong resources/static (giao dien test)
                        .requestMatchers("/*.html").permitAll()

                        // Cho phep truy cap tai lieu API Swagger UI cong khai
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Cac Request khac bat buoc phai duoc xac thuc (co JWT hop le)
                        .anyRequest().authenticated()
                );

        // Them bo loc JWT vao truoc UsernamePasswordAuthenticationFilter mac dinh cua Spring
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 4. Ngan Spring Boot tu dang ky JwtAuthenticationFilter (vi la @Component) thanh servlet filter
    //    toan cuc, tranh chay filter 2 lan cho moi request. Filter chi chay ben trong Security chain.
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
