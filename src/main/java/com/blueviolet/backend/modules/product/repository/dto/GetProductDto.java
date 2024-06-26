package com.blueviolet.backend.modules.product.repository.dto;

import com.blueviolet.backend.modules.product.service.dto.GetProductResult;
import com.querydsl.core.annotations.QueryProjection;

public record GetProductDto(
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
    public GetProductDto {
    }

    public GetProductResult toServiceDto() {
        return new GetProductResult(
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
