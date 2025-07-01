package com.keepflower.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.keepflower.api.common.error_code.ErrorCode;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseBody(
        String message,
        ErrorCode errorCode,
        @Nullable Map<String, List<Map<String, String>>> errors) implements ResponseBody {
    public ErrorResponseBody(String message, ErrorCode errorCode) {
        this(message, errorCode, null);
    }
}
