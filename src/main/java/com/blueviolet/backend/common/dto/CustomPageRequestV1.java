package com.blueviolet.backend.common.dto;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record CustomPageRequestV1(
        @Min(1) Integer page,
        @Min(10) Integer size
) {

    public CustomPageRequestV1 {
        if (page == null) { page = 1; }
        if (size == null) { size = 10; }
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
