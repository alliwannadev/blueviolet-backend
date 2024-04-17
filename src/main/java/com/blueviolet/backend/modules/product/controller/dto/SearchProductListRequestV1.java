package com.blueviolet.backend.modules.product.controller.dto;

import com.blueviolet.backend.modules.product.service.dto.SearchProductListCond;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

public record SearchProductListRequestV1(
        Long categoryId,
        List<String> colors,
        List<String> sizes,
        String priceRange,
        String sortStandard,
        Boolean isInStock
) {
    public SearchProductListCond toDto() {
        SearchProductListCond.PriceRange newPriceRange;
        if (ObjectUtils.isEmpty(priceRange)) {
            newPriceRange = null;
        } else {
            String[] priceArray = priceRange.split("~");
            newPriceRange = new SearchProductListCond.PriceRange(
                    Long.parseLong(priceArray[0]),
                    Long.parseLong(priceArray[1])
            );
        }

        return new SearchProductListCond(
                categoryId,
                colors,
                sizes,
                newPriceRange,
                sortStandard,
                isInStock
        );
    }
}
