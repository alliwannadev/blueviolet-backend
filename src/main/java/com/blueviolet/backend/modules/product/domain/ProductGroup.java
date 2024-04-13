package com.blueviolet.backend.modules.product.domain;

import com.blueviolet.backend.common.domain.BaseTimeEntity;
import com.blueviolet.backend.modules.category.domain.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductGroup extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String productGroupName;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductGroup(
            Category category,
            String productGroupName
    ) {
        this.category = category;
        this.productGroupName = productGroupName;
    }

    public static ProductGroup of(
            Category category,
            String productGroupName
    ) {
        ProductGroup productGroup = ProductGroup
                .builder()
                .productGroupName(productGroupName)
                .build();

        // 연관관계 설정
        productGroup.changeCategory(category);

        return productGroup;
    }

    // 연관관계 편의 메소드
    public void changeCategory(Category category) {
        this.category = category;
    }
}
