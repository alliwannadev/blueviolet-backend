package com.blueviolet.backend.modules.order.controller.dto;

import com.blueviolet.backend.common.constant.OrderStatus;
import com.blueviolet.backend.common.util.OrderNoUtil;
import com.blueviolet.backend.modules.order.service.dto.CreateOrderParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequestV1(
        @NotBlank String receiverName,
        @NotBlank String receiverPhoneNumber,
        @NotBlank String receiverAddr,
        @NotBlank String receiverPostalCode,
        @Positive Long totalAmount,
        @Positive Long discountedTotalAmount,
        @Positive Long totalQuantity,
        @Valid @NotEmpty List<OrderItem> orderItemList
) {

    public record OrderItem(
            @Positive Long productOptionCombinationId,
            @Positive Long price,
            @Positive Long discountedPrice,
            @Positive Long quantity,
            @Positive Long subTotalAmount,
            @Positive Long discountedSubTotalAmount
    ) {
    }

    public CreateOrderParam toDto(Long userId) {
        return new CreateOrderParam(
                userId,
                OrderNoUtil.createOrderNo(),
                OrderStatus.ORDER_REQUEST,
                receiverName,
                receiverPhoneNumber,
                receiverAddr,
                receiverPostalCode,
                totalAmount,
                discountedTotalAmount,
                totalQuantity,
                orderItemList
                        .stream()
                        .map(orderItem ->
                                new CreateOrderParam.OrderItemDto(
                                        orderItem.productOptionCombinationId,
                                        orderItem.price,
                                        orderItem.discountedPrice,
                                        orderItem.quantity,
                                        orderItem.subTotalAmount,
                                        orderItem.discountedSubTotalAmount
                                )
                        )
                        .toList()
        );
    }
}
