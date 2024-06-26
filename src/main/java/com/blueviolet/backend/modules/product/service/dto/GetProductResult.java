package com.blueviolet.backend.modules.product.service.dto;

import com.blueviolet.backend.modules.product.controller.dto.GetProductResponseV1;

public record GetProductResult(
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

    public GetProductResponseV1 toResponse() {
        return new GetProductResponseV1(
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
