package com.blueviolet.backend.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.dto.TokenInfo;
import com.blueviolet.backend.common.properties.JwtProperties;
import com.blueviolet.backend.modules.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    public static final int ACCESS_TOKEN_EXPIRATION_SECONDS = 60 * 60; // 60 분 (Minutes)
    public static final int REFRESH_TOKEN_EXPIRATION_SECONDS = 60 * 24 * 60 * 60; // 60 일 (Days)
    public static final long ACCESS_TOKEN_EXPIRATION_MILLISECONDS = ACCESS_TOKEN_EXPIRATION_SECONDS * 1000L;
    public static final long REFRESH_TOKEN_EXPIRATION_MILLISECONDS = REFRESH_TOKEN_EXPIRATION_SECONDS * 1000L;

    public static final String AUTHORITIES_KEY = "authorities";
    public static final String USER_ID_KEY = "userId";
    public static final String GRANT_TYPE = "Bearer";

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final JwtProperties jwtProperties;

    /**
     * Access Token 생성
     */
    public String generateAccessToken(User user) {
        long now = (new Date()).getTime();
        Date tokenExpiration = new Date(now + ACCESS_TOKEN_EXPIRATION_MILLISECONDS);
        return generateToken(
                user,
                tokenExpiration,
                TokenType.ACCESS_TOKEN
        );
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(User user) {
        long now = (new Date()).getTime();
        Date tokenExpiration = new Date(now + REFRESH_TOKEN_EXPIRATION_MILLISECONDS);
        return generateToken(
                user,
                tokenExpiration,
                TokenType.REFRESH_TOKEN
        );
    }

    /**
     * TokenInfo 생성
     */
    public TokenInfo generateTokenInfo(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return TokenInfo.of(
                GRANT_TYPE,
                accessToken,
                refreshToken
        );
    }

    /**
     * 토큰 타입 기반으로 Token 생성
     */
    private String generateToken(
            User user,
            Date tokenExpiration,
            TokenType tokenType
    ) {
        return createToken(user, tokenExpiration, getSecret(tokenType));
    }

    /**
     * Token 유효성 검증
     */
    public boolean validateToken(String token, TokenType tokenType) {
        try {
            byte[] secret = getSecret(tokenType);
            getClaims(token, secret);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못 되었습니다.");
        }

        return false;
    }

    /**
     * 토큰 파싱
     */
    public Claims parseToken(String token, TokenType tokenType) {
        byte[] secret = getSecret(tokenType);
        return getClaims(token, secret);
    }

    /**
     * 토큰 기반으로 userId 조회
     */
    public Long getUserId(String token, TokenType tokenType) {
        Claims claims = parseToken(token, tokenType);
        return claims.get(USER_ID_KEY, Long.class);
    }

    /**
     * 토큰 유효 기간이 지났는지 확인
     */
    public boolean isTokenExpired(String token, TokenType tokenType) {
        Claims claims = parseToken(token, tokenType);
        Date expiredAt = claims.getExpiration();
        return expiredAt.before(new Date());
    }

    /**
     * SigningKey 조회
     */
    private Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

    /**
     * 토큰 생성
     */
    private String createToken(
            User user,
            Date tokenExpiration,
            byte[] secretKey
    ) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim(USER_ID_KEY, user.getUserId())
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .setExpiration(tokenExpiration)
                .compact();

        return token;
    }

    /**
     * 토큰 기반으로 Claim 조회
     */
    private Claims getClaims(String token, byte[] secretKey) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Token 타입을 이용하여 Secret 값을 byte[]으로 조회
     */
    private byte[] getSecret(TokenType tokenType) {
        byte[] secret;
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                String accessSecret = jwtProperties.getAccessSecret();
                secret = accessSecret.getBytes(StandardCharsets.UTF_8);
            }
            case REFRESH_TOKEN -> {
                String refreshSecret = jwtProperties.getRefreshSecret();
                secret = refreshSecret.getBytes(StandardCharsets.UTF_8);
            }
            default -> throw new RuntimeException("잘못된 토큰 타입입니다.");
        }

        return secret;
    }
}
