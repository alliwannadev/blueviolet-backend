package com.blueviolet.backend.modules.option.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductOption extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long productOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_group_id")
    private ProductOptionGroup productOptionGroup;

    private String productOptionCode;

    private String productOptionName;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductOption(
            ProductOptionGroup productOptionGroup,
            String productOptionCode,
            String productOptionName
    ) {
        this.productOptionGroup = productOptionGroup;
        this.productOptionCode = productOptionCode;
        this.productOptionName = productOptionName;
    }

    public static ProductOption of(
            ProductOptionGroup productOptionGroup,
            String productOptionCode,
            String productOptionName
    ) {
        ProductOption productOption = ProductOption
                .builder()
                .productOptionCode(productOptionCode)
                .productOptionName(productOptionName)
                .build();

        // 연관관계 설정
        productOption.changeProductOptionGroup(productOptionGroup);

        return productOption;
    }

    // 연관관계 편의 메소드
    public void changeProductOptionGroup(ProductOptionGroup productOptionGroup) {
        this.productOptionGroup = productOptionGroup;
    }
}
