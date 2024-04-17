package com.blueviolet.backend.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record CustomPageResponseV1<T>(
        List<T> content,
        List<Integer> pageNumbers,
        int currentPage,
        long totalCount,
        int totalPages,
        Boolean isPrevious,
        Boolean isNext,
        int prevPage,
        int nextPage
) {

    public static <T> CustomPageResponseV1<T> of(
            Page<T> page,
            CustomPageRequestV1 customPageRequest
    ) {
        return of(page.getContent(), customPageRequest, page.getTotalElements());
    }

    public static <T> CustomPageResponseV1<T> of(
            List<T> content,
            CustomPageRequestV1 customPageRequest,
            long totalCount
    ) {
        int currentPage = customPageRequest.page();
        int pageSize = customPageRequest.size();
        int displayPageSize = 10;
        int tempEndPage = (int) (Math.ceil(currentPage / (double) displayPageSize)) * displayPageSize;
        int startPage = tempEndPage - (displayPageSize - 1);

        int totalPages = (int) (Math.ceil(totalCount / (double) pageSize));
        int endPage = Math.min(totalPages, tempEndPage);

        boolean isPrevious = startPage > 1;
        boolean isNext = endPage < totalPages;
        int prevPage = isPrevious ? startPage - 1 : 0;
        int nextPage = isNext ? endPage + 1 : 0;

        List<Integer> pageNumbers =
                IntStream
                        .rangeClosed(startPage, endPage)
                        .boxed()
                        .collect(Collectors.toList());

        return new CustomPageResponseV1<>(
                content,
                pageNumbers.isEmpty() ? List.of(currentPage) : pageNumbers,
                currentPage,
                totalCount,
                totalPages,
                isPrevious,
                isNext,
                prevPage,
                nextPage
        );
    }
}
