package com.blueviolet.backend.modules.option.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import com.blueviolet.backend.modules.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductOptionCombination extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionCombinationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String combinationCode;

    private String uniqueCombinationCode;

    private String combinationName;

    private String size;

    private String color;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductOptionCombination(
            Product product,
            String combinationCode,
            String uniqueCombinationCode,
            String combinationName,
            String size,
            String color
    ) {
        this.product = product;
        this.combinationCode = combinationCode;
        this.uniqueCombinationCode = uniqueCombinationCode;
        this.combinationName = combinationName;
        this.size = size;
        this.color = color;
    }

    public static ProductOptionCombination of(
            Product product,
            String combinationCode,
            String uniqueCombinationCode,
            String combinationName,
            String size,
            String color
    ) {
        ProductOptionCombination productOptionCombination = ProductOptionCombination
                .builder()
                .combinationCode(combinationCode)
                .uniqueCombinationCode(uniqueCombinationCode)
                .combinationName(combinationName)
                .size(size)
                .color(color)
                .build();

        // 연관관계 설정
        productOptionCombination.changeProduct(product);

        return productOptionCombination;
    }

    // 연관관계 편의 메소드
    public void changeProduct(Product product) {
        this.product = product;
    }
}
