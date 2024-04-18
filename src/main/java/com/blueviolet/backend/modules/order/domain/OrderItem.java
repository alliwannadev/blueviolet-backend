package com.blueviolet.backend.modules.order.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productOptionCombinationId;

    private Long price;

    private Long discountedPrice;

    private Long quantity;

    @Builder(access = AccessLevel.PRIVATE)
    private OrderItem(
            Order order,
            Long productOptionCombinationId,
            Long price,
            Long discountedPrice,
            Long quantity
    ) {
        this.order = order;
        this.productOptionCombinationId = productOptionCombinationId;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.quantity = quantity;
    }

    public static OrderItem of(
            Order order,
            Long productOptionCombinationId,
            Long price,
            Long discountedPrice
    ) {
        OrderItem orderItem = OrderItem
                .builder()
                .productOptionCombinationId(productOptionCombinationId)
                .price(price)
                .discountedPrice(discountedPrice)
                .build();

        // 연관관계 설정
        orderItem.changeOrder(order);

        return orderItem;
    }

    // 연관관계 편의 메소드
    public void changeOrder(Order order) {
        this.order = order;
    }
}
