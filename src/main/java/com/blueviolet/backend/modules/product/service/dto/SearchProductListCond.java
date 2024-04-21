package com.blueviolet.backend.modules.product.service.dto;

import java.util.List;

public record SearchProductListCond(
        Long categoryId,
        List<String> colors,
        List<String> sizes,
        PriceRange priceRange,
        String sortStandard,
        Boolean isInStock
) {
    public record PriceRange(
            Long minPrice,
            Long maxPrice
    ) {
    }
}
