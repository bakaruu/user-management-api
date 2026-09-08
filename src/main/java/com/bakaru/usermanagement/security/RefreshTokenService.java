package com.bakaru.usermanagement.security;

import com.bakaru.usermanagement.exception.InvalidCredentialsException;
import com.bakaru.usermanagement.user.entity.RefreshToken;
import com.bakaru.usermanagement.user.entity.User;
import com.bakaru.usermanagement.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String generate(User user) {
        String rawToken = generateSecureRandomToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(rawToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    public User validateAndGetUser(String rawToken) {
        RefreshToken token = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new InvalidCredentialsException("Refresh token has been revoked");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Refresh token has expired");
        }

        return token.getUser();
    }

    public void revoke(String rawToken) {
        refreshTokenRepository.findByToken(rawToken).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}