package com.blueviolet.backend.modules.option.helper;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.option.domain.ProductOption;
import com.blueviolet.backend.modules.option.domain.ProductOptionCombination;
import com.blueviolet.backend.modules.option.repository.ProductOptionCombinationRepository;
import com.blueviolet.backend.modules.option.repository.ProductOptionRepository;
import com.blueviolet.backend.modules.product.domain.Product;
import com.blueviolet.backend.modules.product.helper.dto.CreateTestProductParam;
import com.blueviolet.backend.modules.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductOptionDbHelper {

    private final ProductRepository productRepository;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final ProductOptionRepository productOptionRepository;
    private final EntityManager em;

    @Transactional
    public void createProductOptionAndCombination(CreateTestProductParam createTestProductParam) {
        createTestProductParam
                .productList()
                .forEach(product -> {
                    String productCode = product.productCode();
                    String productName = product.productName();

                    for (CreateTestProductParam.SelectedOption selectedOption : product.selectedOptionList()) {
                        String optionCombinationCode =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .map(CreateTestProductParam.SelectedOptionValue::optionValue)
                                        .collect(Collectors.joining("-"));
                        String optionCombinationName =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .map(CreateTestProductParam.SelectedOptionValue::optionName)
                                        .collect(Collectors.joining("-"));
                        String filteringOption =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .map(optionValue -> optionValue.optionCode() + "-" + optionValue.optionValue())
                                        .collect(Collectors.joining(","));
                        String productOptionCombinationCode = productCode + "-" + optionCombinationCode;
                        String productOptionCombinationName = productName + "-" + optionCombinationName;
                        ProductOptionCombination savedProductOptionCombination =
                                createProductOptionCombination(
                                        productCode,
                                        productOptionCombinationCode,
                                        productOptionCombinationName,
                                        filteringOption
                                );

                        selectedOption
                                .selectedOptionValues()
                                .forEach(
                                        selectedOptionValue -> createProductOption(
                                                savedProductOptionCombination,
                                                selectedOptionValue.optionCode(),
                                                selectedOptionValue.optionName(),
                                                selectedOptionValue.optionValue(),
                                                selectedOptionValue.optionValueName()
                                        )
                                );
                    }
                });
    }

    @Transactional
    public ProductOption createProductOption(
            ProductOptionCombination productOptionCombination,
            String optionCode,
            String optionName,
            String optionValue,
            String optionValueName
    ) {
        return productOptionRepository.save(
                ProductOption.of(
                        productOptionCombination,
                        optionCode,
                        optionName,
                        optionValue,
                        optionValueName
                )
        );
    }

    @Transactional
    public ProductOptionCombination createProductOptionCombination(
            String productCode,
            String productOptionCombinationCode,
            String productOptionCombinationName,
            String filteringOption
    ) {
        return productOptionCombinationRepository.save(
                ProductOptionCombination.of(
                        getOneByProductCode(productCode),
                        productOptionCombinationCode,
                        getUniqueCombinationCode(productOptionCombinationCode),
                        productOptionCombinationName,
                        filteringOption
                )
        );
    }

    @Transactional(readOnly = true)
    public Product getOneByProductCode(String productCode) {
        return productRepository
                .findOneByProductCode(productCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private String getUniqueCombinationCode(String productCombinationCode) {
        char[] productCombinationCharacters =
                productCombinationCode
                        .toLowerCase()
                        .replace("-", "")
                        .toCharArray();
        Arrays.sort(productCombinationCharacters);
        return String.valueOf(productCombinationCharacters);
    }
}
