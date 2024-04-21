package com.blueviolet.backend.common.web.resolvers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomUser {

    private Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private CustomUser(
            Long userId
    ) {
        this.userId = userId;
    }

    public static CustomUser of(
            Long userId
    ) {
        return CustomUser
                .builder()
                .userId(userId)
                .build();
    }

}
