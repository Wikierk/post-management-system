package com.jowk.common.api;

import java.util.List;

public record ListResponse<T>(
        List<T> items,
        int totalCount
) {

    public ListResponse(List<T> items) {
        this(items, items != null ? items.size() : 0);
    }

    public static <T> ListResponse<T> of(List<T> items) {
        return new ListResponse<>(items, items != null ? items.size() : 0);
    }

}
