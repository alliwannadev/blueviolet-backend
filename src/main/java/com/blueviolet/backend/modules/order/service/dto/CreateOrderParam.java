package com.blueviolet.backend.modules.order.service.dto;

import com.blueviolet.backend.common.constant.OrderStatus;
import com.blueviolet.backend.modules.order.domain.Order;
import com.blueviolet.backend.modules.order.domain.OrderItem;

import java.util.List;

public record CreateOrderParam(
        Long userId,
        String orderNo,
        OrderStatus orderStatus,
        String receiverName,
        String receiverPhoneNumber,
        String receiverAddr,
        String receiverPostalCode,
        Long totalAmount,
        Long discountedTotalAmount,
        Long totalQuantity,
        List<OrderItemDto> orderItemDtoList
) {

    public record OrderItemDto(
            Long productOptionCombinationId,
            Long price,
            Long discountedPrice,
            Long quantity,
            Long subTotalAmount,
            Long discountedSubTotalAmount
    ) {
    }

    public List<OrderItem> toOrderItemList(
            Order order
    ) {
        return orderItemDtoList
                .stream()
                .map(orderItemDto ->
                        OrderItem.of(
                                order,
                                orderItemDto.productOptionCombinationId,
                                orderItemDto.price,
                                orderItemDto.discountedPrice,
                                orderItemDto.quantity,
                                orderItemDto.subTotalAmount,
                                orderItemDto.discountedSubTotalAmount
                        )
                )
                .toList();
    }

    public Order toOrder() {
        return Order.of(
                userId,
                orderNo,
                orderStatus,
                receiverName,
                receiverPhoneNumber,
                receiverAddr,
                receiverPostalCode,
                totalAmount,
                discountedTotalAmount,
                totalQuantity
        );
    }
}
