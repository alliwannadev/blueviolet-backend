package com.blueviolet.backend.modules.order.controller.dto;

import com.blueviolet.backend.modules.order.service.dto.CreateOrderResult;

import java.util.List;

public record CreateOrderResponseV1(
        Long orderId,
        Long userId,
        String orderNo,
        String orderStatus,
        String receiverName,
        String receiverPhoneNumber,
        String receiverAddr,
        String receiverPostalCode,
        Long totalAmount,
        Long discountedTotalAmount,
        Long totalQuantity,
        List<OrderItemDto> orderItems
) {

    public record OrderItemDto(
            Long orderItemId,
            Long productOptionCombinationId,
            Long price,
            Long discountedPrice,
            Long quantity,
            Long subTotalAmount,
            Long discountedSubTotalAmount
    ) {
    }

    public static CreateOrderResponseV1 fromDto(
            CreateOrderResult createOrderResult
    ) {
        return new CreateOrderResponseV1(
                createOrderResult.orderId(),
                createOrderResult.userId(),
                createOrderResult.orderNo(),
                createOrderResult.orderStatus().getCode(),
                createOrderResult.receiverName(),
                createOrderResult.receiverPhoneNumber(),
                createOrderResult.receiverAddr(),
                createOrderResult.receiverPostalCode(),
                createOrderResult.totalAmount(),
                createOrderResult.discountedTotalAmount(),
                createOrderResult.totalQuantity(),
                createOrderResult.orderItems()
                        .stream()
                        .map(orderItem -> new OrderItemDto(
                                orderItem.orderItemId(),
                                orderItem.productOptionCombinationId(),
                                orderItem.price(),
                                orderItem.discountedPrice(),
                                orderItem.quantity(),
                                orderItem.subTotalAmount(),
                                orderItem.discountedSubTotalAmount()
                        ))
                        .toList()
        );
    }
}
