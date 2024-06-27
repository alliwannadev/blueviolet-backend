package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductSortingStandard {
    PRICE_LOW("PRICE_LOW", "낮은 가격순"),
    PRICE_HIGH("PRICE_HIGH", "높은 가격순"),
    NEWEST("NEWEST", "신상품순"),
    DISCOUNT_HIGH("DISCOUNT_HIGH", "할인율순"),
    DEFAULT("DEFAULT", "기본 값");

    private final String code;
    private final String description;

    public static ProductSortingStandard getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return ProductSortingStandard.DEFAULT;
        }

        return Arrays.stream(ProductSortingStandard.values())
                .filter(constant -> code.equalsIgnoreCase(constant.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
