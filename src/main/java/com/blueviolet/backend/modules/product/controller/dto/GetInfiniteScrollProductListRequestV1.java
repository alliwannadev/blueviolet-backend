package com.blueviolet.backend.modules.product.controller.dto;

import jakarta.validation.constraints.Positive;

public record GetInfiniteScrollProductListRequestV1(
        @Positive Long categoryId,
        Long lastProductId,
        @Positive Integer size
) {
}
