package com.blueviolet.backend.modules.admin.product.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminProductQueryService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getOneByProductCode(String productCode) {
        return productRepository
                .findOneByProductCode(productCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
