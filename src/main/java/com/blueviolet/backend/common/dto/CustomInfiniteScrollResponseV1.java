package com.blueviolet.backend.common.dto;

import java.util.List;

public record CustomInfiniteScrollResponseV1<T>(
        List<T> content,
        Boolean isNext
) {
    public static <T> CustomInfiniteScrollResponseV1<T> of(
            List<T> content,
            int pageSize
    ) {
        if (content.size() > pageSize) {
            List<T> limitedContent = content
                    .stream()
                    .limit(pageSize)
                    .toList();
            return new CustomInfiniteScrollResponseV1<>(
                    limitedContent,
                    true
            );

        }

        return new CustomInfiniteScrollResponseV1<>(
                content,
                false
        );
    }
}
