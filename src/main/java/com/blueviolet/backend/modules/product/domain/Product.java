package com.blueviolet.backend.modules.product.domain;

import com.blueviolet.backend.common.converter.BooleanConverter;
import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;

    private String productCode;

    private String productName;

    private String modelName;

    private Long purchasePrice;

    private Long sellingPrice;

    private String description;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "display_yn")
    private Boolean isDisplayed;

    @Builder(access = AccessLevel.PRIVATE)
    private Product(
            ProductGroup productGroup,
            String productCode,
            String productName,
            String modelName,
            Long purchasePrice,
            Long sellingPrice,
            String description,
            Boolean isDisplayed
    ) {
        this.productGroup = productGroup;
        this.productCode = productCode;
        this.productName = productName;
        this.modelName = modelName;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.description = description;
        this.isDisplayed = isDisplayed;
    }

    public static Product of(
            ProductGroup productGroup,
            String productCode,
            String productName,
            String modelName,
            Long purchasePrice,
            Long sellingPrice,
            String description,
            Boolean isDisplayed
    ) {
        Product product = Product
                .builder()
                .productCode(productCode)
                .productName(productName)
                .modelName(modelName)
                .purchasePrice(purchasePrice)
                .sellingPrice(sellingPrice)
                .description(description)
                .isDisplayed(isDisplayed)
                .build();

        // 연관관계 설정
        product.changeProductGroup(productGroup);

        return product;
    }

    // 연관관계 편의 메소드
    public void changeProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }
}
