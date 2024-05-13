package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OptionCode {
    SIZE("SIZE", "사이즈"),
    COLOR("COLOR", "색상");

    private final String code;
    private final String description;

    public static OptionCode getByCode(String code) {
        return Arrays.stream(OptionCode.values())
                .filter(constant -> code.equalsIgnoreCase(constant.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
