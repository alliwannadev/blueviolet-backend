package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ExchangeStatus {
    EXCHANGE_REQUEST("EXG_REQ", "교환 요청"),
    EXCHANGE_COMPLETED("EXG_CPD", "교환 완료"),
    EXCHANGE_CANCELLED("EXG_CLD", "교환 취소");

    private final String code;
    private final String description;

    public static ExchangeStatus getByCode(String code) {
        return Arrays.stream(ExchangeStatus.values())
                .filter(status -> code.equalsIgnoreCase(status.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
