package com.bakaru.usermanagement.user.dto;

import com.bakaru.usermanagement.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;
    private Role role;
    private UserResponse user;


}