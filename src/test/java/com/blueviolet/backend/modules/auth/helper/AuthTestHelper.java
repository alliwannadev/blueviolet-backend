package com.blueviolet.backend.modules.auth.helper;

import com.blueviolet.backend.common.dto.TokenInfo;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignInRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignUpRequestV1;
import com.blueviolet.backend.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AuthTestHelper {

    private final AuthService authService;

    @Transactional
    public void signUp(SignUpRequestV1 signUpRequestV1) {
        authService.signUp(signUpRequestV1);
    }

    @Transactional
    public TokenInfo signIn(SignInRequestV1 signInRequestV1) {
        return authService.signIn(signInRequestV1);
    }
}
