package com.dev.backend.dto.response;

import com.dev.backend.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private UUID id;
    private String name;
    private String address;
    private Boolean status;
    private String email;
    private String message;
    private ArrayList<String> roles;
}
