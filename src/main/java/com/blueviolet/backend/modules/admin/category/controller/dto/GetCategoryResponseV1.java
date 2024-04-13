package com.blueviolet.backend.modules.admin.category.controller.dto;

import com.blueviolet.backend.modules.admin.category.service.dto.GetCategoryResult;

public record GetCategoryResponseV1(
        Long categoryId,
        Long parentCategoryId,
        String name,
        Integer level
) {

    public static GetCategoryResponseV1 fromDto(GetCategoryResult categoryResult) {
        return new GetCategoryResponseV1(
                categoryResult.categoryId(),
                categoryResult.parentCategoryId(),
                categoryResult.name(),
                categoryResult.level()
        );
    }
}
