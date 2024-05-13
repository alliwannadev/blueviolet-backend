package com.blueviolet.backend.modules.product.controller.dto;

import com.blueviolet.backend.modules.product.service.dto.SearchProductListParam;
import jakarta.validation.constraints.Positive;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

public record SearchProductListRequestV1(
        @Positive Long categoryId,
        List<String> colors,
        List<String> sizes,
        String priceRange,
        String sortStandard,
        Boolean isInStock
) {
    public SearchProductListParam toDto() {
        SearchProductListParam.PriceRange newPriceRange;
        if (ObjectUtils.isEmpty(priceRange)) {
            newPriceRange = null;
        } else {
            String[] priceArray = priceRange.split("~");
            newPriceRange = new SearchProductListParam.PriceRange(
                    Long.parseLong(priceArray[0]),
                    Long.parseLong(priceArray[1])
            );
        }

        return new SearchProductListParam(
                categoryId,
                colors,
                sizes,
                newPriceRange,
                sortStandard,
                isInStock
        );
    }
}
