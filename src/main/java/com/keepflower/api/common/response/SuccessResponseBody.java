package com.keepflower.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponseBody<T>(String message, @Nullable T data) implements ResponseBody {
    public SuccessResponseBody(String message) {
        this(message, null);
    }
}
