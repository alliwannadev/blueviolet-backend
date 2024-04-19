package com.blueviolet.backend.modules.order.controller;

import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.common.web.resolvers.CurrentUser;
import com.blueviolet.backend.common.web.resolvers.CustomUser;
import com.blueviolet.backend.modules.order.controller.dto.CreateOrderRequestV1;
import com.blueviolet.backend.modules.order.controller.dto.CreateOrderResponseV1;
import com.blueviolet.backend.modules.order.service.OrderService;
import com.blueviolet.backend.modules.order.service.dto.CreateOrderResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class OrderApiV1 {

    private final OrderService orderService;

    @PostMapping(OrderApiPathsV1.V1_ORDERS)
    public OkResponse<CreateOrderResponseV1> createOrder(
            @Valid @RequestBody CreateOrderRequestV1 createOrderRequestV1,
            @CurrentUser CustomUser customUser
    ) {
        CreateOrderResult createOrderResult = orderService.create(createOrderRequestV1.toDto(customUser.getUserId()));
        return OkResponse.of(CreateOrderResponseV1.fromDto(createOrderResult));
    }
}
