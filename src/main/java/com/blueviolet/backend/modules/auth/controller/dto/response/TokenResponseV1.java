package com.blueviolet.backend.modules.auth.controller.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponseV1 {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private TokenResponseV1(
            String grantType,
            String accessToken,
            String refreshToken
    ) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenResponseV1 of(
            String grantType,
            String accessToken,
            String refreshToken
    ) {
        return TokenResponseV1.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
