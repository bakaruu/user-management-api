package com.bakaru.usermanagement.user.dto;

import com.bakaru.usermanagement.user.entity.Role;
import com.bakaru.usermanagement.user.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}