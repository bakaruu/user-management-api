package com.bakaru.usermanagement.user.controller;

import com.bakaru.usermanagement.user.dto.UpdateUserRequest;
import com.bakaru.usermanagement.user.dto.UserResponse;
import com.bakaru.usermanagement.user.entity.UserStatus;
import com.bakaru.usermanagement.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user profile and admin management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get my profile", description = "Returns the authenticated user's profile")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMe(userId));
    }

    @Operation(summary = "Update my profile", description = "Updates the authenticated user's own data")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated")
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(Authentication authentication,
                                                 @Valid @RequestBody UpdateUserRequest request) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateMe(userId, request));
    }

    @Operation(summary = "Get all users", description = "Admin only - returns list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Admin only - returns a specific user")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user", description = "Admin only - updates any user's data")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Change user status", description = "Admin only - activates or suspends a user")
    @ApiResponse(responseCode = "204", description = "Status changed successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUserStatus(@PathVariable UUID id,
                                                 @RequestBody String status) {
        userService.changeUserStatus(id, UserStatus.valueOf(status.replace("\"", "")));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user", description = "Admin only - permanently deletes a user")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
