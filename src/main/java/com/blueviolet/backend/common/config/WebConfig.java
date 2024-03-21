package com.blueviolet.backend.common.config;

import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.web.interceptors.AuthenticationCheckInterceptor;
import com.blueviolet.backend.common.web.resolvers.CurrentUserArgumentResolver;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.modules.user.service.UserService;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인증 체크 인터셉터
        List<String> staticResourcePaths = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .toList();
        List<String> excludePathPatterns =
                List.of(
                        "/v1/sign-up", "/v1/sign-in", "/v1/token-regeneration"
                );

        registry.addInterceptor(
                new AuthenticationCheckInterceptor(
                        jwtTokenProvider,
                        userService
                ))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns)
                .excludePathPatterns(staticResourcePaths);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver(jwtTokenProvider));
    }
}