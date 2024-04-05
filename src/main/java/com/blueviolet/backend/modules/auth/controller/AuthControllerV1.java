package com.blueviolet.backend.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.common.dto.TokenInfo;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignInRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignOutRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignUpRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.TokenRegenerationRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.response.TokenResponseV1;
import com.blueviolet.backend.modules.auth.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class AuthControllerV1 {

    private final AuthService authService;

    @PostMapping(AuthApiPathsV1.V1_SIGN_UP)
    public OkResponse<Void> signUp(
            @RequestBody @Valid SignUpRequestV1 signUpRequestV1
    ) {
        authService.signUp(signUpRequestV1);
        return OkResponse.of("회원가입을 완료했습니다.");
    }

    @PostMapping(AuthApiPathsV1.V1_SIGN_IN)
    public OkResponse<TokenResponseV1> signIn(
            @RequestBody @Valid SignInRequestV1 signInRequestV1
    ) {
        TokenInfo tokenInfo = authService.signIn(signInRequestV1);

        return OkResponse.of(
                TokenResponseV1.of(
                        tokenInfo.getGrantType(),
                        tokenInfo.getAccessToken(),
                        tokenInfo.getRefreshToken()
                )
        );
    }

    @PostMapping(AuthApiPathsV1.V1_SIGN_OUT)
    public OkResponse<Void> signOut(
            @RequestBody @Valid SignOutRequestV1 signOutRequestV1
    ) {
        String refreshTokenValue = signOutRequestV1.getRefreshToken();
        authService.signOut(refreshTokenValue);

        return OkResponse.of("로그아웃을 완료했습니다.");
    }

    @PostMapping(AuthApiPathsV1.V1_TOKEN_REGENERATION)
    public OkResponse<TokenResponseV1> regenerateAccessTokenByRefreshToken(
            @Valid @RequestBody TokenRegenerationRequestV1 tokenRegenerationRequestV1
    ) {
        String regeneratedAccessToken = authService.regenerateAccessTokenByRefreshToken(
                tokenRegenerationRequestV1.getRefreshToken()
        );

        return OkResponse.of(
                TokenResponseV1.of(
                        JwtTokenProvider.GRANT_TYPE,
                        regeneratedAccessToken,
                        tokenRegenerationRequestV1.getRefreshToken()
                )
        );
    }
}
