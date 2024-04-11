package com.blueviolet.backend.modules.admin.category.service.dto;

import com.blueviolet.backend.modules.category.domain.Category;
import org.apache.commons.lang3.ObjectUtils;

public record GetCategoryResult(
        Long categoryId,
        Long parentCategoryId,
        String name,
        Integer level
) {

    public static GetCategoryResult fromEntity(Category category) {
        return new GetCategoryResult(
                category.getCategoryId(),
                ObjectUtils.isEmpty(category.getParentCategory()) ? null : category.getParentCategory().getCategoryId(),
                category.getName(),
                category.getLevel()
        );
    }
}
