package com.blueviolet.backend.modules.product.controller.dto;

public record SearchProductResponseV1(
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

}
