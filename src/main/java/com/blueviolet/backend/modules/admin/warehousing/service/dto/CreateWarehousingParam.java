package com.blueviolet.backend.modules.admin.warehousing.service.dto;

public record CreateWarehousingParam(
        Long productOptionCombinationId,
        String productOptionCombinationName,
        String warehousingDate,
        Long quantity
) {
}
