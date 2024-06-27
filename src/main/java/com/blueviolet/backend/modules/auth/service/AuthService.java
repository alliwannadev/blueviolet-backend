package com.blueviolet.backend.modules.auth.service;

import com.blueviolet.backend.modules.user.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.dto.TokenInfo;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignInRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignUpRequestV1;
import com.blueviolet.backend.modules.auth.domain.RefreshToken;
import com.blueviolet.backend.modules.user.domain.User;
import com.blueviolet.backend.modules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthValidationService authValidationService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    private final UserCacheRepository userCacheRepository;

    @Transactional
    public void signUp(SignUpRequestV1 signUpRequestV1) {
        String rawPassword = signUpRequestV1.getPassword();
        userService.saveNewUser(signUpRequestV1.toUserEntity(
                passwordEncoder.encode(rawPassword)
        ));
    }

    @Transactional
    public TokenInfo signIn(SignInRequestV1 signInRequestV1) {
        User findUser = userService
                .getOptionalOneByEmail(signInRequestV1.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));
        userCacheRepository.save(findUser);
        authValidationService.validateIfMatchesPassword(
                signInRequestV1.getPassword(),
                findUser.getPassword()
        );

        TokenInfo tokenInfo = jwtTokenProvider.generateTokenInfo(findUser);
        upsertRefreshTokenByUserId(
                findUser.getUserId(),
                tokenInfo.getRefreshToken()
        );

        return tokenInfo;
    }

    @Transactional
    public void signOut(String refreshTokenValue) {
        if (StringUtils.isBlank(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        refreshTokenService.deleteByRefreshTokenValue(refreshTokenValue);
    }

    @Transactional(readOnly = true)
    public String regenerateAccessTokenByRefreshToken(
            String refreshTokenValue
    ) {
        RefreshToken refreshToken = refreshTokenService.getByRefreshTokenValue(refreshTokenValue);
        refreshTokenService.validateRefreshToken(refreshToken.getTokenValue());
        User user = userService.getOneByUserId(refreshToken.getUserId());

        return jwtTokenProvider.generateAccessToken(user);
    }

    private void upsertRefreshTokenByUserId(
            Long userId,
            String newRefreshTokenValue
    ) {
        refreshTokenService.upsertByUserId(
                userId,
                newRefreshTokenValue
        );
    }
}
