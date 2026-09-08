package com.bakaru.usermanagement.user.controller;

import com.bakaru.usermanagement.user.dto.AuthResponse;
import com.bakaru.usermanagement.user.dto.LoginRequest;
import com.bakaru.usermanagement.user.dto.RefreshTokenRequest;
import com.bakaru.usermanagement.user.dto.RegisterRequest;
import com.bakaru.usermanagement.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with USER role")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "409", description = "Email or DNI already exists")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT access token and refresh token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials or account suspended")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @Operation(summary = "Refresh access token", description = "Exchanges a valid refresh token for a new 15-minute access token")
    @ApiResponse(responseCode = "200", description = "New access token issued")
    @ApiResponse(responseCode = "401", description = "Refresh token invalid, expired, or revoked")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(userService.refreshToken(request));
    }

    @Operation(summary = "Logout", description = "Revokes the given refresh token server-side")
    @ApiResponse(responseCode = "204", description = "Logged out successfully")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        userService.logout(request);
        return ResponseEntity.noContent().build();
    }
}