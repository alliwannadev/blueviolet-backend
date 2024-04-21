package com.blueviolet.backend.modules.auth.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    private Long userId;

    private String tokenValue;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(Long userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }

    public static RefreshToken of(
            Long userId,
            String tokenValue
    ) {
        return RefreshToken.builder()
                .userId(userId)
                .tokenValue(tokenValue)
                .build();
    }

    public void changeTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
