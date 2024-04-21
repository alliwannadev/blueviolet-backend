package com.blueviolet.backend.modules.admin.product.controller.dto;

import com.blueviolet.backend.modules.admin.product.service.dto.CreateProductParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductRequestV1(
        @NotNull Long categoryId,
        @NotBlank String productGroupName,
        @Valid @NotEmpty List<Product> productList
) {

    public record Product(
            @NotBlank String productCode,
            @NotBlank String productName,
            @NotBlank String modelName,
            @Valid @NotEmpty List<SelectedOption> selectedOptionList,
            @NotNull Long purchasePrice,
            @NotNull Long sellingPrice,
            @NotBlank String description
    ) {
    }

    public record SelectedOption(
            @Valid @NotEmpty List<SelectedOptionValue> selectedOptionValues
    ) {
    }

    public record SelectedOptionValue(
            @NotBlank String optionCode,
            @NotBlank String optionName,
            @NotBlank String optionValue,
            @NotBlank String optionValueName
    ) {
    }

    public CreateProductParam toDto() {
        return new CreateProductParam(
                categoryId,
                productGroupName,
                productList
                        .stream()
                        .map(CreateProductParam.ProductDto::of)
                        .toList()
        );
    }
}
