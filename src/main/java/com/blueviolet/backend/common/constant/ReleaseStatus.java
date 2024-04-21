package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ReleaseStatus {

    RELEASE_REQUEST("RELSE_REQ", "출고 요청"),
    RELEASE_IN_PROGRESS("RELSE_IN", "출고 처리 중"),
    RELEASE_COMPLETED("RELSE_CPD", "출고 완료");

    private final String code;
    private final String description;

    public static ReleaseStatus getByCode(String code) {
        return Arrays.stream(ReleaseStatus.values())
                .filter(status -> code.equalsIgnoreCase(status.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
