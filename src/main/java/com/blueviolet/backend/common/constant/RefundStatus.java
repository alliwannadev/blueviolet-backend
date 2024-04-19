package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RefundStatus {
    REFUND_REQUEST("RFD_REQ", "환불 요청"),
    REFUND_IN_PROGRESS("RFD_IN", "환불 처리 중"),
    REFUND_COMPLETED("RFD_CPD", "환불 완료"),
    REFUND_CANCELLED("RFD_CLD", "환불 취소");

    private final String code;
    private final String description;

    public static RefundStatus getByCode(String code) {
        return Arrays.stream(RefundStatus.values())
                .filter(status -> code.equalsIgnoreCase(status.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
