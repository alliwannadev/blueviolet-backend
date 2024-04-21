package com.blueviolet.backend.modules.admin.category.service.dto;

import java.util.List;

public record CreateCategoryParam(
        List<CategoryDto> categories
) {

    public record CategoryDto(
            String name,
            Integer level,
            String categoryKey,
            String parentCategoryKey
    ) {
    }
}
