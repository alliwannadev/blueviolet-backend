package com.blueviolet.backend.modules.auth.repository;

import com.blueviolet.backend.modules.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenValue(String tokenValue);

    Optional<RefreshToken> findByUserId(Long userId);
}
