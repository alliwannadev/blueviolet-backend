package com.blueviolet.backend.modules.admin.product.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.repository.CategoryRepository;
import com.blueviolet.backend.modules.product.domain.ProductGroup;
import com.blueviolet.backend.modules.product.repository.ProductGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminProductGroupService {

    private final CategoryRepository categoryRepository;
    private final ProductGroupRepository productGroupRepository;

    @Transactional
    public ProductGroup createProductGroup(Long categoryId, String productGroupName) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        return productGroupRepository.save(
                ProductGroup.of(
                        category,
                        productGroupName
                ));
    }
}
