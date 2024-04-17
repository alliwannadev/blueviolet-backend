package com.blueviolet.backend.modules.admin.warehosing.controller.dto;

import com.blueviolet.backend.modules.admin.warehosing.service.dto.CreateWarehousingParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateWarehousingRequestV1(
        @Positive Long productOptionCombinationId,
        @NotBlank String productOptionCombinationName,
        @NotBlank String warehousingDate,
        @Positive Long quantity
) {

    public CreateWarehousingParam toDto() {
        return new CreateWarehousingParam(
                productOptionCombinationId,
                productOptionCombinationName,
                warehousingDate,
                quantity
        );
    }
}
