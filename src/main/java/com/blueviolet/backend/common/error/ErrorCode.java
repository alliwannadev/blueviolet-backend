package com.blueviolet.backend.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "COMMON-001", "잘못된 값을 입력하셨습니다."),
    METHOD_NOT_ALLOWED(405, "COMMON-002", "허용하지 않는 HTTP Method 입니다."),
    DATA_NOT_FOUND(404, "COMMON-003", "요청하신 데이터를 찾을 수 없습니다."),
    DATA_ACCESS_ERROR(500, "COMMON-004", "요청하신 데이터에 접근하는 과정에서 에러가 발생했습니다."),
    INTERNAL_SERVER_ERROR(500, "COMMON-005", "서버에 에러가 발생했습니다.")
    ;

    private final int status;

    private final String code;

    private final String message;
}
