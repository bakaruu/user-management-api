package com.bakaru.usermanagement.dto;

import com.bakaru.usermanagement.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;
    private Role role;
    private UserResponse user;


}