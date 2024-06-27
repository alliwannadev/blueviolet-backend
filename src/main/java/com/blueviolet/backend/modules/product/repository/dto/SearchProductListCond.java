package com.blueviolet.backend.modules.product.repository.dto;

import java.util.List;

public record SearchProductListCond(
        List<Long> categoryIds,
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
