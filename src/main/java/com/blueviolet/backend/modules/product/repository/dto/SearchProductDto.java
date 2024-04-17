package com.blueviolet.backend.modules.product.repository.dto;

import com.blueviolet.backend.modules.product.service.dto.SearchProductResult;
import com.querydsl.core.annotations.QueryProjection;

public record SearchProductDto(
        Long productId,
        Long productGroupId,
        String productCode,
        String productName,
        String modelName,
        Long purchasePrice,
        Long sellingPrice,
        String description,
        Boolean isDisplayed
) {

    @QueryProjection
    public SearchProductDto {
    }

    public SearchProductResult toServiceDto() {
        return new SearchProductResult(
                productId,
                productGroupId,
                productCode,
                productName,
                modelName,
                purchasePrice,
                sellingPrice,
                description,
                isDisplayed
        );
    }
}
