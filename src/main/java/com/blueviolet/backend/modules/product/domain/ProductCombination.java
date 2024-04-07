package com.blueviolet.backend.modules.product.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductCombination extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCombinationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productCombinationCode;

    private String uniqueCombinationCode;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductCombination(
            Product product,
            String productCombinationCode,
            String uniqueCombinationCode
    ) {
        this.product = product;
        this.productCombinationCode = productCombinationCode;
        this.uniqueCombinationCode = uniqueCombinationCode;
    }

    public static ProductCombination of(
            Product product,
            String productCombinationCode,
            String uniqueCombinationCode
    ) {
        ProductCombination productCombination = ProductCombination
                .builder()
                .productCombinationCode(productCombinationCode)
                .uniqueCombinationCode(uniqueCombinationCode)
                .build();

        // 연관관계 설정
        productCombination.changeProduct(product);

        return productCombination;
    }

    // 연관관계 편의 메소드
    public void changeProduct(Product product) {
        this.product = product;
    }
}
