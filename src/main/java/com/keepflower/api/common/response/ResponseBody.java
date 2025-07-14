package com.keepflower.api.common.response;

public sealed interface ResponseBody permits SuccessResponseBody, ErrorResponseBody {
    String message();
}
