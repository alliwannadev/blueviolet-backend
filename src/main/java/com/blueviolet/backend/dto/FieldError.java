package com.blueviolet.backend.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldError {
    private String field;
    private String value;
    private String reason;

    private FieldError(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public static List<FieldError> of(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public static FieldError of(
            String field,
            String value,
            String reason
    ) {
        return new FieldError(
                field,
                StringUtils.isBlank(value) ? "" : value,
                reason
        );
    }
}