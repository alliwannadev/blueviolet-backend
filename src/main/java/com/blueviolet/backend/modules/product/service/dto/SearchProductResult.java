package com.blueviolet.backend.modules.product.service.dto;

import com.blueviolet.backend.modules.product.controller.dto.SearchProductResponseV1;
import com.blueviolet.backend.modules.product.domain.Product;

public record SearchProductResult(
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

    public SearchProductResponseV1 toResponse() {
        return new SearchProductResponseV1(
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

    public static SearchProductResult fromEntity(Product product) {
        return new SearchProductResult(
                product.getProductId(),
                product.getProductGroup().getProductGroupId(),
                product.getProductCode(),
                product.getProductName(),
                product.getModelName(),
                product.getPurchasePrice(),
                product.getSellingPrice(),
                product.getDescription(),
                product.getIsDisplayed()
        );
    }
}
