package com.blueviolet.backend.modules.admin.product.controller;

import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.modules.admin.product.controller.dto.CreateProductRequestV1;
import com.blueviolet.backend.modules.admin.product.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class AdminProductApiV1 {

    private final AdminProductService adminProductService;

    @PostMapping(AdminProductApiPathsV1.V1_PRODUCTS)
    public OkResponse<Void> createProduct(
            @Valid @RequestBody CreateProductRequestV1 createProductRequestV1
    ) {
        adminProductService.createProduct(
                createProductRequestV1.toDto()
        );

        return OkResponse.empty();
    }
}
