package com.blueviolet.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@NoArgsConstructor
public class OkResponse<T> {

    @JsonInclude(NON_NULL)
    private T data;

    private String message;

    private LocalDateTime timestamp;

    @Builder(access = AccessLevel.PRIVATE)
    private OkResponse(
            T data,
            String message,
            LocalDateTime timestamp
    ) {
        this.data = data;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static <T> OkResponse<T> of(T data) {
        return OkResponse.<T> builder()
                .data(data)
                .message("API 요청을 완료하였습니다.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static OkResponse<Void> of(String message) {
        return OkResponse.<Void> builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> OkResponse<T> of(
            T data,
            String message
    ) {
        return OkResponse.<T> builder()
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}