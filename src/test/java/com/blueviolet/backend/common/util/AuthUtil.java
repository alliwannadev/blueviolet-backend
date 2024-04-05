package com.blueviolet.backend.common.util;

public class AuthUtil {

    public static String createToken(
            String grantType,
            String tokenValue
    ) {
        return String.format(
                "%s %s",
                grantType,
                tokenValue
        );
    }
}
