package com.blueviolet.backend.modules.order.domain;

import com.blueviolet.backend.common.converter.OrderStatusConverter;
import com.blueviolet.backend.common.domain.BaseTimeEntity;
import com.blueviolet.backend.common.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId;

    private String orderNo;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus;

    private String receiverName;

    private String receiverPhoneNumber;

    private String receiverAddr;

    private String receiverPostalCode;

    private Long totalPrice;

    private Long totalDiscountedPrice;

    private Long totalQuantity;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(
            Long userId,
            String orderNo,
            OrderStatus orderStatus,
            String receiverName,
            String receiverPhoneNumber,
            String receiverAddr,
            String receiverPostalCode,
            Long totalPrice,
            Long totalDiscountedPrice,
            Long totalQuantity
    ) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.receiverAddr = receiverAddr;
        this.receiverPostalCode = receiverPostalCode;
        this.totalPrice = totalPrice;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.totalQuantity = totalQuantity;
    }

    public static Order of(
            Long userId,
            String orderNo,
            OrderStatus orderStatus,
            String receiverName,
            String receiverPhoneNumber,
            String receiverAddr,
            String receiverPostalCode,
            Long totalPrice,
            Long totalDiscountedPrice,
            Long totalQuantity
    ) {
        return Order
                .builder()
                .userId(userId)
                .orderNo(orderNo)
                .orderStatus(orderStatus)
                .receiverName(receiverName)
                .receiverPhoneNumber(receiverPhoneNumber)
                .receiverAddr(receiverAddr)
                .receiverPostalCode(receiverPostalCode)
                .totalPrice(totalPrice)
                .totalDiscountedPrice(totalDiscountedPrice)
                .totalQuantity(totalQuantity)
                .build();
    }
}
