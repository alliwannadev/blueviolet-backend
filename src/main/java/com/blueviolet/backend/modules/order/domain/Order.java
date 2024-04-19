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
@Table(name = "ORDERS")
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

    private Long totalAmount;

    private Long discountedTotalAmount;

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
            Long totalAmount,
            Long discountedTotalAmount,
            Long totalQuantity
    ) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.receiverAddr = receiverAddr;
        this.receiverPostalCode = receiverPostalCode;
        this.totalAmount = totalAmount;
        this.discountedTotalAmount = discountedTotalAmount;
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
            Long totalAmount,
            Long discountedTotalAmount,
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
                .totalAmount(totalAmount)
                .discountedTotalAmount(discountedTotalAmount)
                .totalQuantity(totalQuantity)
                .build();
    }
}
