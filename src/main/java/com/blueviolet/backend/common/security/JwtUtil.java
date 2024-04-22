package com.blueviolet.backend.common.security;

import org.springframework.util.StringUtils;

public class JwtUtil {

    public static String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
