package com.blueviolet.backend.common.web.interceptors;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.common.security.JwtUtil;
import com.blueviolet.backend.common.security.TokenType;
import com.blueviolet.backend.modules.admin.authority.service.AdminAuthorityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class AdminAuthorityCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final AdminAuthorityService adminAuthorityService;

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
            if (!adminAuthorityService.existsAdminRoleByUserId(userId)) {
                throw new BusinessException(ErrorCode.ADMIN_AUTHORITY_NOT_FOUND);
            }

            return true;
        }

        throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
}
