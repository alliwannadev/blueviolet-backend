package com.blueviolet.backend.modules.product.helper.dto;

import java.util.List;

public record CreateTestProductParam(
        Long categoryId,
        String productGroupName,
        List<ProductDto> productList
) {

    public record ProductDto(
            String productCode,
            String productName,
            String modelName,
            List<SelectedOption> selectedOptionList,
            Long purchasePrice,
            Long sellingPrice,
            String description
    ) {
    }

    public record SelectedOption(
            List<SelectedOptionValue> selectedOptionValues
    ) {
    }

    public record SelectedOptionValue(
            String optionCode,
            String optionName,
            String optionValue,
            String optionValueName
    ) {
    }
}
