package com.blueviolet.backend.modules.admin.category.controller.dto;

import com.blueviolet.backend.modules.admin.category.service.dto.CreateCategoryParam;

import java.util.List;

public record CreateCategoryRequestV1(
        List<Category> categories
) {
    public record Category(
            String name,
            Integer level,
            String categoryKey,
            String parentCategoryKey
    ) {
    }

    public CreateCategoryParam toDto() {
        return new CreateCategoryParam(
                categories
                        .stream()
                        .map((category ->
                                new CreateCategoryParam.CategoryDto(
                                        category.name,
                                        category.level,
                                        category.categoryKey,
                                        category.parentCategoryKey
                                )))
                        .toList()
        );
    }
}
