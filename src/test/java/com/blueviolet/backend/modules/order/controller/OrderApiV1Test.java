package com.blueviolet.backend.modules.order.controller;

import com.blueviolet.backend.common.annotation.IntegrationTest;
import com.blueviolet.backend.common.dto.TokenInfo;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.util.AuthUtil;
import com.blueviolet.backend.common.util.JsonUtil;
import com.blueviolet.backend.modules.admin.warehousing.service.dto.CreateWarehousingParam;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignInRequestV1;
import com.blueviolet.backend.modules.auth.controller.dto.request.SignUpRequestV1;
import com.blueviolet.backend.modules.auth.helper.AuthTestHelper;
import com.blueviolet.backend.modules.category.helper.CategoryTestHelper;
import com.blueviolet.backend.modules.option.domain.ProductOptionCombination;
import com.blueviolet.backend.modules.option.helper.ProductOptionCombinationTestHelper;
import com.blueviolet.backend.modules.order.controller.dto.CreateOrderRequestV1;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.helper.ProductTestHelper;
import com.blueviolet.backend.modules.warehousing.helper.ProductWarehousingTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("통합 테스트 - 주문 API V1")
@IntegrationTest
class OrderApiV1Test {

    @Autowired MockMvc mockMvc;
    @Autowired JsonUtil jsonUtil;

    @Autowired AuthTestHelper authTestHelper;
    @Autowired CategoryTestHelper categoryTestHelper;
    @Autowired ProductTestHelper productTestHelper;
    @Autowired ProductOptionCombinationTestHelper productOptionCombinationTestHelper;
    @Autowired ProductWarehousingTestHelper productWarehousingTestHelper;

    @BeforeEach
    void beforeEach() {
        authTestHelper.signUp(
                new SignUpRequestV1(
                        "tester@test.com",
                        "123456",
                        "dh",
                        "01011112222"
                )
        );
        productTestHelper.createProductWithDefaultValue();
        ProductOptionCombination foundCombination = productOptionCombinationTestHelper.getOneByCombinationCode("ADIDAS-CLOTH-001-BLACK-M");
        productWarehousingTestHelper.createWarehousing(
                new CreateWarehousingParam(
                        foundCombination.getProductOptionCombinationId(),
                        foundCombination.getCombinationName(),
                        "20240419",
                        5L
                )
        );
    }

    @DisplayName("[API][POST][SUCCESS] 주문 등록 API 호출")
    @Test
    void givenOrderParameters_whenCreateOrder_thenReturnSuccessfulResponse() throws Exception {
        // Given
        TokenInfo tokenInfo = authTestHelper.signIn(new SignInRequestV1("tester@test.com", "123456"));
        ProductOptionCombination productOptionCombination = productOptionCombinationTestHelper.getOneByCombinationCode("ADIDAS-CLOTH-001-BLACK-M");
        Product product = productTestHelper.getOneByProductId(productOptionCombination.getProduct().getProductId());

        CreateOrderRequestV1 createOrderRequestV1 =
                new CreateOrderRequestV1(
                        "테스터",
                        "01011112222",
                        "서울특별시 강남구 역삼동 000-00",
                        "11111",
                        400L,
                        400L,
                        2L,
                        List.of(
                               new CreateOrderRequestV1.OrderItem(
                                       productOptionCombination.getProductOptionCombinationId(),
                                       product.getSellingPrice(),
                                       product.getSellingPrice(),
                                       2L,
                                       product.getSellingPrice() * 2L,
                                       product.getSellingPrice() * 2L
                               )
                        )
                );
        String requestBody = jsonUtil.toJson(createOrderRequestV1);

        // When & Then
        mockMvc.perform(
                        post(OrderApiPathsV1.V1_ORDERS)
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthUtil.createToken(
                                                tokenInfo.getGrantType(),
                                                tokenInfo.getAccessToken()
                                        )
                                )
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("API 요청을 완료하였습니다."));
    }

    @DisplayName("[API][POST][FAIL] 주문 등록 API 호출 - 잘못된 파라미터를 전달하는 경우")
    @Test
    void givenInvalidOrderParameters_whenCreateOrder_thenReturnFailedResponse() throws Exception {
        // Given
        TokenInfo tokenInfo = authTestHelper.signIn(new SignInRequestV1("tester@test.com", "123456"));
        CreateOrderRequestV1 createOrderRequestV1 =
                new CreateOrderRequestV1(
                        "",
                        "",
                        "",
                        "",
                        400L,
                        400L,
                        2L,
                        new ArrayList<>()
                );
        String requestBody = jsonUtil.toJson(createOrderRequestV1);

        // When & Then
        mockMvc.perform(
                        post(OrderApiPathsV1.V1_ORDERS)
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthUtil.createToken(
                                                tokenInfo.getGrantType(),
                                                tokenInfo.getAccessToken()
                                        )
                                )
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
    }
}