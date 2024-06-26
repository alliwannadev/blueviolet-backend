package com.blueviolet.backend.modules.product.service.dto;

import com.blueviolet.backend.modules.product.repository.dto.SearchProductListCond;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

public record SearchProductListParam(
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

    public SearchProductListCond toCondition(List<Long> categoryIds) {
        return new SearchProductListCond(
                categoryIds,
                colors,
                sizes,
                ObjectUtils.isEmpty(priceRange) ? null :
                new SearchProductListCond.PriceRange(
                        priceRange.minPrice,
                        priceRange.maxPrice
                ),
                sortStandard,
                isInStock
        );
    }
}
