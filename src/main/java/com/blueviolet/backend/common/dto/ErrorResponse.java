package com.blueviolet.backend.common.dto;

import com.blueviolet.backend.common.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String code;

    private String message;

    private List<FieldError> errors;

    private LocalDateTime timestamp;

    private ErrorResponse(
            ErrorCode code,
            String message
    ) {
        this.code = code.getCode();
        this.message = StringUtils.isBlank(message) ? code.getMessage() : message;
        this.errors = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
    }

    private ErrorResponse(
            ErrorCode code,
            String message,
            List<FieldError> errors
    ) {
        this.code = code.getCode();
        this.message = StringUtils.isBlank(message) ? code.getMessage() : message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    private ErrorResponse(
            ErrorCode code,
            List<FieldError> errors
    ) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code, "");
    }

    public static ErrorResponse of(
            ErrorCode code,
            String message
    ) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(
            ErrorCode code,
            String message,
            List<FieldError> errors
    ) {
        return new ErrorResponse(code, message, errors);
    }

    public static ErrorResponse of(
            ErrorCode code,
            BindingResult bindingResult
    ) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }
}