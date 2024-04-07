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
public class ProductOptionGroup extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productOptionGroupCode;

    private String productOptionGroupName;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductOptionGroup(
            Product product,
            String productOptionGroupCode,
            String productOptionGroupName
    ) {
        this.product = product;
        this.productOptionGroupCode = productOptionGroupCode;
        this.productOptionGroupName = productOptionGroupName;
    }

    public static ProductOptionGroup of(
            Product product,
            String productOptionGroupCode,
            String productOptionGroupName
    ) {
        ProductOptionGroup productOptionGroup = ProductOptionGroup
                .builder()
                .productOptionGroupCode(productOptionGroupCode)
                .productOptionGroupName(productOptionGroupName)
                .build();

        // 연관관계 설정
        productOptionGroup.changeProduct(product);

        return productOptionGroup;
    }

    // 연관관계 편의 메소드
    public void changeProduct(Product product) {
        this.product = product;
    }
}
