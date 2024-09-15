package com.inventrevo.movieapi.dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> data,
        Integer pageNumber,
        Integer pageSize,
        int totalPages,
        Long totalElements,
        boolean isLast
) {
}
