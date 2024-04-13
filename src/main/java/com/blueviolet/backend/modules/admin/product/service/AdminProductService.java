package com.blueviolet.backend.modules.admin.product.service;

import com.blueviolet.backend.modules.admin.option.AdminProductOptionService;
import com.blueviolet.backend.modules.admin.product.service.dto.CreateProductParam;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.domain.ProductGroup;
import com.blueviolet.backend.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminProductService {

    private final AdminProductGroupService adminProductGroupService;
    private final AdminProductOptionService adminProductOptionService;

    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(CreateProductParam createProductParam) {
        ProductGroup productGroup = adminProductGroupService.createProductGroup(
                createProductParam.categoryId(),
                createProductParam.productGroupName()
        );

        List<Product> products = createProductParam
                .productList()
                .stream()
                .map(product -> Product.of(
                        productGroup,
                        product.productCode(),
                        product.productName(),
                        product.modelName(),
                        product.purchasePrice(),
                        product.sellingPrice(),
                        product.description(),
                        true
                ))
                .toList();
        productRepository.saveAll(products);

        adminProductOptionService.createProductOptionAndCombination(
                createProductParam
        );
    }
}
