package com.blueviolet.backend.modules.product.controller;

import com.blueviolet.backend.common.annotation.IntegrationTest;
import com.blueviolet.backend.common.dto.CustomPageRequestV1;
import com.blueviolet.backend.common.dto.CustomPageResponseV1;
import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.common.util.JsonUtil;
import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.helper.CategoryTestHelper;
import com.blueviolet.backend.modules.product.controller.dto.SearchProductResponseV1;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.helper.ProductTestHelper;
import com.blueviolet.backend.modules.product.service.ProductService;
import com.blueviolet.backend.modules.product.service.dto.SearchProductListCond;
import com.blueviolet.backend.modules.product.service.dto.SearchProductResult;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("통합 테스트 - 상품 API V1")
@IntegrationTest
class ProductApiV1Test {

    @Autowired MockMvc mockMvc;
    @Autowired JsonUtil jsonUtil;
    @Autowired CategoryTestHelper categoryTestHelper;
    @Autowired ProductTestHelper productTestHelper;

    @Autowired ProductService productService;

    @BeforeEach
    void beforeEach() {
        productTestHelper.createProductWithDefaultValue();
    }

    @DisplayName("[API][GET][SUCCESS] 상품 목록 조회 API 호출")
    @Test
    void givenProductSearchParameters_whenSearchProductList_thenReturnSuccessfulResult() throws Exception {
        // Given
        Category foundCategory = categoryTestHelper.findOneByPathName("남성>상의>반소매 티셔츠");

        // When & Then
        String jsonResponse = mockMvc.perform(
                        get(ProductApiPathsV1.V1_PRODUCTS)
                                .queryParam("categoryId", String.valueOf(foundCategory.getCategoryId()))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.message").value("API 요청을 완료하였습니다."))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        OkResponse<CustomPageResponseV1<SearchProductResponseV1>> searchProductListResponse =
                jsonUtil.fromJson(
                        jsonResponse,
                        new TypeReference<>() {}
                );
        List<SearchProductResponseV1> contentInApiResponse = searchProductListResponse.getData().content();

        Page<SearchProductResult> searchProductResult = productService.searchAllByCond(
                new SearchProductListCond(
                        foundCategory.getCategoryId(),
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                new CustomPageRequestV1(1, 10).getPageable()
        );
        List<SearchProductResult> contentInDatabase = searchProductResult.getContent();

        for (int i = 0; i < contentInApiResponse.size(); i++) {
            SearchProductResponseV1 productResponse = contentInApiResponse.get(i);
            SearchProductResult product = contentInDatabase.get(i);

            assertThat(productResponse.productId()).isEqualTo(product.productId());
            assertThat(productResponse.productGroupId()).isEqualTo(product.productGroupId());
            assertThat(productResponse.productCode()).isEqualTo(product.productCode());
            assertThat(productResponse.productName()).isEqualTo(product.productName());
            assertThat(productResponse.modelName()).isEqualTo(product.modelName());

            assertThat(productResponse.purchasePrice()).isEqualTo(product.purchasePrice());
            assertThat(productResponse.sellingPrice()).isEqualTo(product.sellingPrice());
            assertThat(productResponse.description()).isEqualTo(product.description());
            assertThat(productResponse.isDisplayed()).isEqualTo(product.isDisplayed());
        }
    }

    @DisplayName("[API][GET][FAIL] 상품 목록 조회 API 호출 - 잘못된 파라미터를 전달하는 경우")
    @Test
    void givenInvalidProductSearchParameters_whenSearchProductList_thenReturnFailedResponse() throws Exception {
        // Given
        Long categoryId = -1L;

        // When & Then
        mockMvc.perform(
                        get(ProductApiPathsV1.V1_PRODUCTS)
                                .queryParam("categoryId", String.valueOf(categoryId))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
    }

    @DisplayName("[API][GET][SUCCESS] 상품 1건 조회 API 호출")
    @Test
    void givenProductId_whenSearchProduct_thenReturnSuccessfulResult() throws Exception {
        // Given
        Product product = productTestHelper.getFirstProductId();
        Long productId = product.getProductId();

        // When & Then
        mockMvc.perform(
                        get(ProductApiPathsV1.V1_PRODUCTS_BY_PRODUCT_ID
                                .replace("{productId}", String.valueOf(productId)))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.productGroupId").value(product.getProductGroup().getProductGroupId()))
                .andExpect(jsonPath("$.data.productCode").value(product.getProductCode()))
                .andExpect(jsonPath("$.data.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.data.modelName").value(product.getModelName()))
                .andExpect(jsonPath("$.data.purchasePrice").value(product.getPurchasePrice()))
                .andExpect(jsonPath("$.data.sellingPrice").value(product.getSellingPrice()))
                .andExpect(jsonPath("$.data.description").value(product.getDescription()))
                .andExpect(jsonPath("$.data.isDisplayed").value(product.getIsDisplayed()))
                .andExpect(jsonPath("$.message").value("API 요청을 완료하였습니다."));
    }

    @DisplayName("[API][GET][FAIL] 상품 1건 조회 API 호출 - 잘못된 상품 ID를 전달하는 경우")
    @Test
    void givenInvalidProductId_whenSearchProduct_thenReturnFailedResponse() throws Exception {
        // Given
        Long productId = -1L;

        // When & Then
        mockMvc.perform(
                        get(ProductApiPathsV1.V1_PRODUCTS_BY_PRODUCT_ID
                                .replace("{productId}", String.valueOf(productId)))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
    }
}