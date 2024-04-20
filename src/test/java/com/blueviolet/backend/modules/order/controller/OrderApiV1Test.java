package com.blueviolet.backend.modules.order.controller;

import com.blueviolet.backend.common.annotation.IntegrationTest;
import com.blueviolet.backend.common.dto.OkResponse;
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
import com.blueviolet.backend.modules.order.controller.dto.CreateOrderResponseV1;
import com.blueviolet.backend.modules.order.domain.Order;
import com.blueviolet.backend.modules.order.domain.OrderItem;
import com.blueviolet.backend.modules.order.helper.OrderTestHelper;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.helper.ProductTestHelper;
import com.blueviolet.backend.modules.stock.helper.StockTestHelper;
import com.blueviolet.backend.modules.warehousing.helper.ProductWarehousingTestHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Autowired OrderTestHelper orderTestHelper;
    @Autowired StockTestHelper stockTestHelper;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

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

        List<CreateOrderRequestV1.OrderItem> requestOrderItemList = List.of(
                new CreateOrderRequestV1.OrderItem(
                        productOptionCombination.getProductOptionCombinationId(),
                        product.getSellingPrice(),
                        product.getSellingPrice(),
                        2L,
                        product.getSellingPrice() * 2L,
                        product.getSellingPrice() * 2L
                )
        );

        Map<Long, Map<String, Long>> stockQuantityMapBeforeOrder = new HashMap<>();
        for (CreateOrderRequestV1.OrderItem orderItem : requestOrderItemList) {
            stockQuantityMapBeforeOrder.putIfAbsent(
                    orderItem.productOptionCombinationId(),
                    new HashMap<>()
            );
            Map<String, Long> innerMap = stockQuantityMapBeforeOrder.get(orderItem.productOptionCombinationId());
            innerMap.put("orderQuantity", orderItem.quantity());
            innerMap.put("stockQuantity", stockTestHelper.getOneByCombinationId(orderItem.productOptionCombinationId()).getQuantity());
        }

        CreateOrderRequestV1 createOrderRequestV1 =
                new CreateOrderRequestV1(
                        "테스터",
                        "01011112222",
                        "서울특별시 강남구 역삼동 000-00",
                        "11111",
                        400L,
                        400L,
                        2L,
                        requestOrderItemList
                );
        String requestBody = jsonUtil.toJson(createOrderRequestV1);

        // When & Then
        String jsonResponse = mockMvc.perform(
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
                .andExpect(jsonPath("$.message").value("API 요청을 완료하였습니다."))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        OkResponse<CreateOrderResponseV1> createOrderResponse =
                jsonUtil.fromJson(
                        jsonResponse,
                        new TypeReference<>() {}
                );
        Order order = orderTestHelper.getOneByOrderIdWithRelationship(createOrderResponse.getData().orderId());
        assertThat(order.getOrderId()).isEqualTo(createOrderResponse.getData().orderId());
        assertThat(order.getUserId()).isEqualTo(createOrderResponse.getData().userId());
        assertThat(order.getOrderNo()).isEqualTo(createOrderResponse.getData().orderNo());
        assertThat(order.getOrderStatus().getCode()).isEqualTo(createOrderResponse.getData().orderStatus());
        assertThat(order.getReceiverName()).isEqualTo(createOrderResponse.getData().receiverName());
        assertThat(order.getReceiverPhoneNumber()).isEqualTo(createOrderResponse.getData().receiverPhoneNumber());
        assertThat(order.getReceiverAddr()).isEqualTo(createOrderResponse.getData().receiverAddr());
        assertThat(order.getReceiverPostalCode()).isEqualTo(createOrderResponse.getData().receiverPostalCode());
        assertThat(order.getTotalAmount()).isEqualTo(createOrderResponse.getData().totalAmount());
        assertThat(order.getDiscountedTotalAmount()).isEqualTo(createOrderResponse.getData().discountedTotalAmount());
        assertThat(order.getTotalQuantity()).isEqualTo(createOrderResponse.getData().totalQuantity());

        List<CreateOrderResponseV1.OrderItemDto> orderItemDtoList = createOrderResponse.getData().orderItems();
        List<OrderItem> orderItems = order.getOrderItems();

        for (int i = 0; i < orderItemDtoList.size(); i++) {
            CreateOrderResponseV1.OrderItemDto orderItemDto = orderItemDtoList.get(i);
            OrderItem orderItem = orderItems.get(i);

            assertThat(orderItemDto.orderItemId()).isEqualTo(orderItem.getOrderItemId());
            assertThat(orderItemDto.productOptionCombinationId()).isEqualTo(orderItem.getProductOptionCombinationId());
            assertThat(orderItemDto.price()).isEqualTo(orderItem.getPrice());
            assertThat(orderItemDto.discountedPrice()).isEqualTo(orderItem.getDiscountedPrice());
            assertThat(orderItemDto.quantity()).isEqualTo(orderItem.getQuantity());
            assertThat(orderItemDto.subTotalAmount()).isEqualTo(orderItem.getSubTotalAmount());
            assertThat(orderItemDto.discountedSubTotalAmount()).isEqualTo(orderItem.getDiscountedSubTotalAmount());
        }

        for (long productOptionCombinationId : stockQuantityMapBeforeOrder.keySet()) {
            Map<String, Long> innerMap = stockQuantityMapBeforeOrder.get(productOptionCombinationId);
            Long stockQuantityBeforeOrder = innerMap.get("stockQuantity");
            Long orderQuantity = innerMap.get("orderQuantity");
            Long stockQuantityAfterOrder = stockTestHelper.getOneByCombinationId(productOptionCombinationId).getQuantity();
            assertThat(stockQuantityAfterOrder).isEqualTo(stockQuantityBeforeOrder - orderQuantity);
        }
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