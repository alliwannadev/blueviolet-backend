package com.blueviolet.backend.common.config;

import com.blueviolet.backend.common.web.interceptors.AdminAuthorityCheckInterceptor;
import com.blueviolet.backend.modules.admin.authority.service.AdminAuthorityService;
import com.blueviolet.backend.modules.user.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.web.interceptors.AuthenticationCheckInterceptor;
import com.blueviolet.backend.common.web.resolvers.CurrentUserArgumentResolver;
import com.blueviolet.backend.common.security.JwtTokenProvider;
import com.blueviolet.backend.modules.user.service.UserService;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AdminAuthorityService adminAuthorityService;

    private final UserCacheRepository userCacheRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인증 체크 인터셉터
        List<String> staticResourcePaths = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .toList();
        List<String> authenticationExcludePathPatterns =
                List.of(
                        "/h2-console/**",
                        "/api/v1/sign-up", "/api/v1/sign-in", "/api/v1/token-regeneration",
                        "/api/v1/products/**"
                );

        registry.addInterceptor(
                new AuthenticationCheckInterceptor(
                        jwtTokenProvider,
                        userService,
                        userCacheRepository
                ))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(authenticationExcludePathPatterns)
                .excludePathPatterns(staticResourcePaths);
        registry.addInterceptor(
                new AdminAuthorityCheckInterceptor(
                        jwtTokenProvider,
                        adminAuthorityService
                ))
                .order(2)
                .addPathPatterns("/api/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver(jwtTokenProvider));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.OPTIONS.name()
                )
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
