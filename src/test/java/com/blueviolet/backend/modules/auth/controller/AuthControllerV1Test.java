package com.blueviolet.backend.modules.auth.controller;

import com.blueviolet.backend.common.annotation.IntegrationTest;
import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.util.AuthUtil;
import com.blueviolet.backend.common.util.JsonUtil;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignInRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignOutRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignUpRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.TokenRegenerationRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.response.TokenResponseV1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("통합 테스트 - 인증 API V1")
@IntegrationTest
class AuthControllerV1Test {

    @Autowired MockMvc mockMvc;
    @Autowired JsonUtil jsonUtil;

    @DisplayName("[API][POST][SUCCESS] 회원가입 API 호출")
    @Test
    void givenUserInfoParameters_whenSigningUp_thenCreateUserAndReturn() throws Exception {
        // Given
        SignUpRequestV1 signUpRequestV1 = new SignUpRequestV1("tester@test.com", "123456", "dh", "01011112222");
        String requestBody = jsonUtil.toJson(signUpRequestV1);

        // When & Then
        callSignUpApiV1(requestBody)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("회원가입을 완료했습니다."));
    }

    private ResultActions callSignUpApiV1(String requestBody) throws Exception {
        return mockMvc.perform(
                post(AuthApiPathsV1.V1_SIGN_UP)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
        );
    }

    @DisplayName("[API][POST][FAIL] 회원가입 API 호출 시, 잘못된 파라미터 값을 전달하는 경우")
    @Test
    void givenWrongUserInfoParameters_whenSigningUp_thenReturnFailedResponse() throws Exception {
        // Given
        SignUpRequestV1 signUpRequestV1 = new SignUpRequestV1(
                null,
                null,
                null,
                null
        );
        String requestBody = jsonUtil.toJson(signUpRequestV1);

        // When & Then
        callSignUpApiV1(requestBody)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
    }

    @DisplayName("[API][POST][SUCCESS] 로그인 API 호출")
    @Test
    void givenSignInInfoParameters_whenSigningIn_thenReturnToken() throws Exception {
        // Given
        SignUpRequestV1 signUpRequestV1 = new SignUpRequestV1("tester@test.com", "123456", "dh", "01011112222");
        callSignUpApiV1(jsonUtil.toJson(signUpRequestV1));
        SignInRequestV1 signInRequestV1 =
                new SignInRequestV1(
                        "tester@test.com",
                        "123456"
                );
        String requestBody = jsonUtil.toJson(signInRequestV1);

        // When & Then
        callSignInApiV1(requestBody)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.grantType").value("Bearer"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    private ResultActions callSignInApiV1(String requestBody) throws Exception {
        return mockMvc.perform(
                post(AuthApiPathsV1.V1_SIGN_IN)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
        );
    }

    @DisplayName("[API][POST][FAIL] 로그인 API 호출 시, 잘못된 패스워드를 전달하는 경우")
    @Test
    void givenWrongPassword_whenSigningIn_thenReturnFailedResponse() throws Exception {
        // Given
        SignUpRequestV1 signUpRequestV1 = new SignUpRequestV1("tester@test.com", "123456", "dh", "01011112222");
        callSignUpApiV1(jsonUtil.toJson(signUpRequestV1));
        SignInRequestV1 signInRequestV1 =
                new SignInRequestV1(
                        "tester@test.com",
                        "111111"
                );
        String requestBody = jsonUtil.toJson(signInRequestV1);

        // When & Then
        callSignInApiV1(requestBody)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_EMAIL_OR_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_EMAIL_OR_PASSWORD.getMessage()));
    }

    @DisplayName("[API][POST][SUCCESS] 로그아웃 API 호출")
    @Test
    void givenRefreshTokenValue_whenSigningOut_thenSuccessfulResponse() throws Exception {
        // Given
        callSignUpApiV1(
                jsonUtil.toJson(
                        new SignUpRequestV1(
                                "tester@test.com",
                                "123456",
                                "dh",
                                "01011112222"
                        )
                )
        );

        String signInJsonResponse =
                callSignInApiV1(
                        jsonUtil.toJson(
                                new SignInRequestV1(
                                        "tester@test.com",
                                        "123456"
                                )
                        )
                )
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);
        OkResponse<TokenResponseV1> signInResponse =
                jsonUtil.fromJson(
                        signInJsonResponse,
                        OkResponse.class,
                        TokenResponseV1.class
                );

        SignOutRequestV1 signOutRequestV1 = new SignOutRequestV1(signInResponse.getData().getRefreshToken());
        String requestBody = jsonUtil.toJson(signOutRequestV1);

        // When & Then
        mockMvc.perform(
                        post(AuthApiPathsV1.V1_SIGN_OUT)
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthUtil.createToken(
                                                signInResponse.getData().getGrantType(),
                                                signInResponse.getData().getAccessToken()
                                        )
                                )
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("로그아웃을 완료했습니다."));
    }

    @DisplayName("[API][POST][SUCCESS] AccessToken 재발급 API 호출")
    @Test
    void givenRefreshTokenValue_whenRegenerateToken_thenSuccessfulResponse() throws Exception {
        // Given
        callSignUpApiV1(
                jsonUtil.toJson(
                        new SignUpRequestV1(
                                "tester@test.com",
                                "123456",
                                "dh",
                                "01011112222"
                        )
                )
        );

        String signInJsonResponse =
                callSignInApiV1(
                        jsonUtil.toJson(
                                new SignInRequestV1(
                                        "tester@test.com",
                                        "123456"
                                )
                        )
                )
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);
        OkResponse<TokenResponseV1> signInResponse =
                jsonUtil.fromJson(
                        signInJsonResponse,
                        OkResponse.class,
                        TokenResponseV1.class
                );

        TokenRegenerationRequestV1 tokenRegenerationRequestV1 = new TokenRegenerationRequestV1(signInResponse.getData().getRefreshToken());
        String requestBody = jsonUtil.toJson(tokenRegenerationRequestV1);

        // When & Then
        mockMvc.perform(
                        post(AuthApiPathsV1.V1_TOKEN_REGENERATION)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.grantType").value("Bearer"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

}