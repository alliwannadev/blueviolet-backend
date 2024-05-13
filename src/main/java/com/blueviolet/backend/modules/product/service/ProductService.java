package com.blueviolet.backend.modules.product.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.category.service.CategoryService;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.repository.ProductQueryRepository;
import com.blueviolet.backend.modules.product.repository.ProductRepository;
import com.blueviolet.backend.modules.product.repository.dto.SearchProductDto;
import com.blueviolet.backend.modules.product.service.dto.SearchProductListParam;
import com.blueviolet.backend.modules.product.service.dto.SearchProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;

    private final CategoryService categoryService;

    public Page<SearchProductResult> searchAllByCond(
            SearchProductListParam searchProductListParam,
            Pageable pageable
    ) {
        List<Long> childCategoryIds = categoryService.getChildCategoryIdsByCurrentId(searchProductListParam.categoryId());
        Page<SearchProductDto> result = productQueryRepository.findAllByCond(
                searchProductListParam.toCondition(childCategoryIds),
                pageable
        );
        return result.map(SearchProductDto::toServiceDto);
    }

    public SearchProductResult searchOneByProductId(
            Long productId
    ) {
        Product foundProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        return SearchProductResult.fromEntity(foundProduct);
    }
}
