package com.blueviolet.backend.modules.product.helper;

import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.helper.CategoryDbHelper;
import com.blueviolet.backend.modules.option.helper.ProductOptionDbHelper;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.domain.ProductGroup;
import com.blueviolet.backend.modules.product.helper.dto.CreateTestProductParam;
import com.blueviolet.backend.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductDbHelper {

    private final ProductRepository productRepository;

    private final CategoryDbHelper categoryDbHelper;
    private final ProductGroupDbHelper productGroupDbHelper;
    private final ProductOptionDbHelper productOptionDbHelper;

    @Transactional
    public void createProduct(CreateTestProductParam createTestProductParam) {
        ProductGroup productGroup = productGroupDbHelper.createProductGroup(
                createTestProductParam.categoryId(),
                createTestProductParam.productGroupName()
        );

        List<Product> products = createTestProductParam
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

        productOptionDbHelper.createProductOptionAndCombination(
                createTestProductParam
        );
    }

    @Transactional
    public void createProductWithDefaultValue() {
        Category category = categoryDbHelper.createWithDefaultValue();

        CreateTestProductParam.ProductDto productDto1 = new CreateTestProductParam.ProductDto(
                "ADIDAS-CLOTH-001",
                "아디다스 티셔츠 - 블랙",
                "MODEL-NAME-001",
                List.of(
                        new CreateTestProductParam.SelectedOption(
                                List.of(
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "COLOR",
                                                "색상",
                                                "BLACK",
                                                "블랙"
                                        ),
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "SIZE",
                                                "상의 사이즈",
                                                "M",
                                                "M - 95"
                                        )
                                )
                        ),
                        new CreateTestProductParam.SelectedOption(
                                List.of(
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "COLOR",
                                                "색상",
                                                "BLACK",
                                                "블랙"
                                        ),
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "SIZE",
                                                "상의 사이즈",
                                                "L",
                                                "L - 100"
                                        )
                                )
                        )
                ),
                100L,
                200L,
                "아디다스 티셔츠 입니다."
        );

        CreateTestProductParam.ProductDto productDto2 = new CreateTestProductParam.ProductDto(
                "ADIDAS-CLOTH-002",
                "아디다스 티셔츠 - 화이트",
                "MODEL-NAME-002",
                List.of(
                        new CreateTestProductParam.SelectedOption(
                                List.of(
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "COLOR",
                                                "색상",
                                                "WHITE",
                                                "화이트"
                                        ),
                                        new CreateTestProductParam.SelectedOptionValue(
                                                "SIZE",
                                                "상의 사이즈",
                                                "M",
                                                "M - 95"
                                        )
                                )
                        )
                ),
                100L,
                200L,
                "아디다스 티셔츠 입니다."
        );

        createProduct(
                new CreateTestProductParam(
                        category.getCategoryId(),
                        "아디다스 티셔츠",
                        List.of(productDto1, productDto2)
                )
        );
    }

    @Transactional(readOnly = true)
    public Product getFirstProductId() {
        Page<Product> page = productRepository.findAll(PageRequest.of(0, 1));
        return page.getContent().get(0);
    }
}
