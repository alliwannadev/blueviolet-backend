package com.blueviolet.backend.modules.product.service.dto;

public record GetProductListForInfiniteScrollParam(
        Long categoryId,
        Long productId,
        int pageSize
) {
}
