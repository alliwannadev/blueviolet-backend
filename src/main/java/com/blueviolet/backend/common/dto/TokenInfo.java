package com.blueviolet.backend.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenInfo {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private TokenInfo(
            String grantType,
            String accessToken,
            String refreshToken
    ) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenInfo of(
            String grantType,
            String accessToken,
            String refreshToken
    ) {
        return TokenInfo
                .builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
