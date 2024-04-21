package com.blueviolet.backend.modules.admin.product.service.dto;

import com.blueviolet.backend.modules.admin.product.controller.dto.CreateProductRequestV1;

import java.util.List;

public record CreateProductParam(
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

        public static ProductDto of(CreateProductRequestV1.Product product) {
            return new ProductDto(
                    product.productCode(),
                    product.productName(),
                    product.modelName(),
                    product.selectedOptionList()
                            .stream()
                            .map(SelectedOption::of)
                            .toList(),
                    product.purchasePrice(),
                    product.sellingPrice(),
                    product.description()
            );
        }
    }

    public record SelectedOption(
            List<SelectedOptionValue> selectedOptionValues
    ) {
        public static SelectedOption of(CreateProductRequestV1.SelectedOption selectedOption) {
            return new SelectedOption(
                    selectedOption
                            .selectedOptionValues()
                            .stream()
                            .map(SelectedOptionValue::of)
                            .toList()
            );
        }
    }

    public record SelectedOptionValue(
            String optionCode,
            String optionName,
            String optionValue,
            String optionValueName
    ) {
        public static SelectedOptionValue of(CreateProductRequestV1.SelectedOptionValue selectedOptionValue) {
            return new SelectedOptionValue(
                    selectedOptionValue.optionCode(),
                    selectedOptionValue.optionName(),
                    selectedOptionValue.optionValue(),
                    selectedOptionValue.optionValueName()
            );
        }
    }
}
