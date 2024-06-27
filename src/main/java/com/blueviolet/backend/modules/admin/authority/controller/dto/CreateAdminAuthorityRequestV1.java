package com.blueviolet.backend.modules.admin.authority.controller.dto;

import jakarta.validation.constraints.Positive;

public record CreateAdminAuthorityRequestV1(
        @Positive Long userId
) {
}
