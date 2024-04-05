package com.blueviolet.backend.common.web.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.common.security.TokenType;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentUserAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
        boolean hasCustomUserType = CustomUser.class.isAssignableFrom(parameter.getParameterType());

        return hasCurrentUserAnnotation && hasCustomUserType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = resolveToken(request);

        if (StringUtils.hasText(token)) {
            if (jwtTokenProvider.validateToken(
                    token,
                    TokenType.ACCESS_TOKEN
            )) {
                Long userId = jwtTokenProvider.getUserId(token, TokenType.ACCESS_TOKEN);
                return CustomUser.of(userId);
            } else {
                return null;
            }
        }

        return null;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
