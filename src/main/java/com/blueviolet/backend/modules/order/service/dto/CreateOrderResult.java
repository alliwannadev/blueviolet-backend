package com.blueviolet.backend.modules.order.service.dto;

import com.blueviolet.backend.common.constant.OrderStatus;
import com.blueviolet.backend.modules.order.domain.Order;
import com.blueviolet.backend.modules.order.domain.OrderItem;

import java.util.List;

public record CreateOrderResult(
        Long orderId,
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

    public static CreateOrderResult fromEntity(
            Order order,
            List<OrderItem> orderItems
    ) {
        return new CreateOrderResult(
                order.getOrderId(),
                order.getUserId(),
                order.getOrderNo(),
                order.getOrderStatus(),
                order.getReceiverName(),
                order.getReceiverPhoneNumber(),
                order.getReceiverAddr(),
                order.getReceiverPostalCode(),
                order.getTotalAmount(),
                order.getDiscountedTotalAmount(),
                order.getTotalQuantity(),
                orderItems
                        .stream()
                        .map(orderItem -> new OrderItemDto(
                                orderItem.getOrderItemId(),
                                orderItem.getProductOptionCombinationId(),
                                orderItem.getPrice(),
                                orderItem.getDiscountedPrice(),
                                orderItem.getQuantity(),
                                orderItem.getSubTotalAmount(),
                                orderItem.getDiscountedSubTotalAmount()
                        ))
                        .toList()
        );
    }
}
