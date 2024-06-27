package com.blueviolet.backend.modules.admin.option;

import com.blueviolet.backend.common.constant.OptionCode;
import com.blueviolet.backend.modules.admin.product.service.AdminProductQueryService;
import com.blueviolet.backend.modules.admin.product.service.dto.CreateProductParam;
import com.blueviolet.backend.modules.option.domain.ProductOption;
import com.blueviolet.backend.modules.option.domain.ProductOptionCombination;
import com.blueviolet.backend.modules.option.repository.ProductOptionCombinationRepository;
import com.blueviolet.backend.modules.option.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminProductOptionService {

    private final AdminProductQueryService adminProductQueryService;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public void createProductOptionAndCombination(CreateProductParam createProductParam) {
        createProductParam
                .productList()
                .forEach(product -> {
                    String productCode = product.productCode();
                    String productName = product.productName();

                    for (CreateProductParam.SelectedOption selectedOption : product.selectedOptionList()) {
                        String optionCombinationCode =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .map(CreateProductParam.SelectedOptionValue::optionValue)
                                        .collect(Collectors.joining("-"));
                        String optionCombinationName =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .map(selectedOptionValue -> selectedOptionValue.optionName() + ": " + selectedOptionValue.optionValueName())
                                        .collect(Collectors.joining(", "));
                        String size =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .filter(selectedOptionValue -> selectedOptionValue.optionCode().equalsIgnoreCase(OptionCode.SIZE.getCode()))
                                        .findFirst()
                                        .map(CreateProductParam.SelectedOptionValue::optionValue)
                                        .orElse(null);
                        String color =
                                selectedOption.selectedOptionValues()
                                        .stream()
                                        .filter(selectedOptionValue -> selectedOptionValue.optionCode().equalsIgnoreCase(OptionCode.COLOR.getCode()))
                                        .findFirst()
                                        .map(CreateProductParam.SelectedOptionValue::optionValue)
                                        .orElse(null);

                        ProductOptionCombination savedProductOptionCombination =
                                createProductOptionCombination(
                                        productCode,
                                        StringUtils.join(productCode, "-", optionCombinationCode),
                                        StringUtils.join(productName, " (", optionCombinationName, ")"),
                                        size,
                                        color
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
            String size,
            String color
    ) {
        return productOptionCombinationRepository.save(
                ProductOptionCombination.of(
                        adminProductQueryService.getOneByProductCode(productCode),
                        productOptionCombinationCode,
                        getUniqueCombinationCode(productOptionCombinationCode),
                        productOptionCombinationName,
                        size,
                        color
                )
        );
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
