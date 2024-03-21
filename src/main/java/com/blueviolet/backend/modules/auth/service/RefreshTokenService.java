package com.blueviolet.backend.modules.auth.service;

import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.common.security.TokenType;
import com.blueviolet.backend.modules.auth.domain.RefreshToken;
import com.blueviolet.backend.modules.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void upsertByUserId(
            Long userId,
            String newRefreshTokenValue
    ) {
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(userId);

        if (findRefreshToken.isPresent()) {
            RefreshToken oldRefreshToken = findRefreshToken.get();
            update(oldRefreshToken.getTokenValue(), newRefreshTokenValue);
        } else {
            RefreshToken newRefreshToken = RefreshToken.of(
                    userId,
                    newRefreshTokenValue
            );
            refreshTokenRepository.save(newRefreshToken);
        }
    }

    @Transactional
    public void update(
            String oldRefreshTokenValue,
            String newRefreshTokenValue
    ) {
        RefreshToken refreshToken = getByRefreshTokenValue(oldRefreshTokenValue);
        refreshToken.changeTokenValue(newRefreshTokenValue);
    }

    @Transactional
    public void deleteByRefreshTokenValue(String refreshTokenValue) {
        refreshTokenRepository
                .findByTokenValue(refreshTokenValue)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public RefreshToken getByRefreshTokenValue(String refreshTokenValue) {
        return refreshTokenRepository
                .findByTokenValue(refreshTokenValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    public void validateRefreshToken(String refreshTokenValue) {
        boolean isValid = jwtTokenProvider.validateToken(
                refreshTokenValue,
                TokenType.REFRESH_TOKEN
        );
        if (!isValid) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_VALUE);
        }
    }
}
