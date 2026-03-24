package com.bakaru.usermanagement.user.service;

import com.bakaru.usermanagement.user.dto.*;
import com.bakaru.usermanagement.user.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserService {

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getMe(UUID id);
    UserResponse updateMe(UUID id, UpdateUserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    UserResponse updateUser(UUID id, UpdateUserRequest request);
    void changeUserStatus(UUID id, UserStatus status);
    void deleteUser(UUID id);
}