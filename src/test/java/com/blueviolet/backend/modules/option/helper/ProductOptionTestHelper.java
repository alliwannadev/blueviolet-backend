package com.blueviolet.backend.modules.option.helper;

import com.blueviolet.backend.modules.admin.option.AdminProductOptionService;
import com.blueviolet.backend.modules.admin.product.service.dto.CreateProductParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ProductOptionTestHelper {

    private final AdminProductOptionService adminProductOptionService;

    @Transactional
    public void createProductOptionAndCombination(CreateProductParam createProductParam) {
        adminProductOptionService.createProductOptionAndCombination(createProductParam);
    }
}
