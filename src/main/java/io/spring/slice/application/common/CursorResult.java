package io.spring.slice.application.common;

import org.springframework.data.domain.Slice;


public record CursorResult<T>(
    Slice<T> valueList,
    Boolean hasNext
) {
}
