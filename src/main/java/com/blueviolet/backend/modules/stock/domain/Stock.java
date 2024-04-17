package com.blueviolet.backend.modules.stock.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private Long productOptionCombinationId;

    private String productOptionCombinationName;

    private Long quantity;

    @Builder(access = AccessLevel.PRIVATE)
    private Stock(
            Long productOptionCombinationId,
            String productOptionCombinationName,
            Long quantity
    ) {
        this.productOptionCombinationId = productOptionCombinationId;
        this.productOptionCombinationName = productOptionCombinationName;
        this.quantity = quantity;
    }

    public static Stock of(
            Long productOptionCombinationId,
            String productOptionCombinationName,
            Long quantity
    ) {
        return Stock
                .builder()
                .productOptionCombinationId(productOptionCombinationId)
                .productOptionCombinationName(productOptionCombinationName)
                .quantity(quantity)
                .build();
    }

    public void updateQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
