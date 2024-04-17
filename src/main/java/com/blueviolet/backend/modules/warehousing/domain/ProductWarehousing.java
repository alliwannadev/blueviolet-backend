package com.blueviolet.backend.modules.warehousing.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import com.blueviolet.backend.modules.stock.domain.Stock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductWarehousing extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehousingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private LocalDate warehousingDate;

    private Long productOptionCombinationId;

    private Long quantity;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductWarehousing(
            Stock stock,
            LocalDate warehousingDate,
            Long productOptionCombinationId,
            Long quantity
    ) {
        this.stock = stock;
        this.warehousingDate = warehousingDate;
        this.productOptionCombinationId = productOptionCombinationId;
        this.quantity = quantity;
    }

    public static ProductWarehousing of(
            Stock stock,
            LocalDate warehousingDate,
            Long productOptionCombinationId,
            Long quantity
    ) {
        ProductWarehousing productWarehousing = ProductWarehousing
                .builder()
                .warehousingDate(warehousingDate)
                .productOptionCombinationId(productOptionCombinationId)
                .quantity(quantity)
                .build();

        // 연관관계 설정
        productWarehousing.changeStock(stock);

        return productWarehousing;
    }

    // 연관관계 편의 메소드
    public void changeStock(Stock stock) {
        this.stock = stock;
    }
}
