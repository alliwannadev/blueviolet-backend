package com.blueviolet.backend.common.web.interceptors;

import com.blueviolet.backend.common.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.common.security.TokenType;
import com.blueviolet.backend.modules.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.MessageFormat;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = JwtUtil.resolveToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (StringUtils.hasText(token) &&
            jwtTokenProvider.validateToken(token, TokenType.ACCESS_TOKEN)
        ) {
            Long userId = jwtTokenProvider.getUserId(token, TokenType.ACCESS_TOKEN);
            userService.getOptionalOneByUserId(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

            return true;
        }

        log.info(
                MessageFormat.format(
                        "유효하지 않은 JWT 토큰 입니다. (토큰 정보 : {0}",
                        token
                )
        );
        throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
}
