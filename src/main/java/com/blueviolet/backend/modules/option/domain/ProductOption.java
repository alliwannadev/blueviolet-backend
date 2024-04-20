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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_combination_id")
    private ProductOptionCombination productOptionCombination;

    private String optionCode;

    private String optionName;

    private String optionValue;

    private String optionValueName;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductOption(
            ProductOptionCombination productOptionCombination,
            String optionCode,
            String optionName,
            String optionValue,
            String optionValueName
    ) {
        this.productOptionCombination = productOptionCombination;
        this.optionCode = optionCode;
        this.optionName = optionName;
        this.optionValue = optionValue;
        this.optionValueName = optionValueName;
    }

    public static ProductOption of(
            ProductOptionCombination productOptionCombination,
            String optionCode,
            String optionName,
            String optionValue,
            String optionValueName
    ) {
        ProductOption productOption = ProductOption
                .builder()
                .optionCode(optionCode)
                .optionName(optionName)
                .optionValue(optionValue)
                .optionValueName(optionValueName)
                .build();

        // 연관관계 설정
        productOption.changeProductOptionGroup(productOptionCombination);

        return productOption;
    }

    // 연관관계 편의 메소드
    public void changeProductOptionGroup(ProductOptionCombination productOptionCombination) {
        this.productOptionCombination = productOptionCombination;
    }
}
