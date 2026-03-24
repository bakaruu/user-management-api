package com.bakaru.usermanagement.user.service;

import com.bakaru.usermanagement.exception.InvalidCredentialsException;
import com.bakaru.usermanagement.exception.ResourceAlreadyExistsException;
import com.bakaru.usermanagement.exception.ResourceNotFoundException;
import com.bakaru.usermanagement.user.dto.AuthResponse;
import com.bakaru.usermanagement.user.dto.LoginRequest;
import com.bakaru.usermanagement.user.dto.RegisterRequest;
import com.bakaru.usermanagement.user.entity.Role;
import com.bakaru.usermanagement.user.entity.User;
import com.bakaru.usermanagement.user.entity.UserStatus;
import com.bakaru.usermanagement.user.repository.UserRepository;
import com.bakaru.usermanagement.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Aru");
        registerRequest.setLastName("Test");
        registerRequest.setDni("12345678A");
        registerRequest.setEmail("aru@test.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("aru@test.com");
        loginRequest.setPassword("password123");

        user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Aru")
                .lastName("Test")
                .dni("12345678A")
                .email("aru@test.com")
                .password("hashedPassword")
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    // REGISTER TESTS
    @Test
    void register_WhenEmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userServiceImpl.register(registerRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_WhenDniAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByDni(registerRequest.getDni())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userServiceImpl.register(registerRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_WhenValidRequest_ReturnsAuthResponse() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByDni(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("token");

        AuthResponse response = userServiceImpl.register(registerRequest);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(userRepository, times(1)).save(any());
    }

    // LOGIN TESTS
    @Test
    void login_WhenUserNotFound_ThrowsException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.login(loginRequest));
    }

    @Test
    void login_WhenAccountSuspended_ThrowsException() {
        user.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> userServiceImpl.login(loginRequest));
    }

    @Test
    void login_WhenInvalidPassword_ThrowsException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> userServiceImpl.login(loginRequest));
    }

    @Test
    void login_WhenValidCredentials_ReturnsAuthResponse() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("token");

        AuthResponse response = userServiceImpl.login(loginRequest);

        assertNotNull(response);
        assertEquals("token", response.getToken());
    }
}