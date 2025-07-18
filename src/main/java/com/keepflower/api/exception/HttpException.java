package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;
import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {
    private final int statusCode;
    private final ErrorCode errorCode;

    public HttpException(ErrorCode errorCode) {
        super(errorCode.name());
        this.statusCode = errorCode.getStatusCode();
        this.errorCode = errorCode;
    }
}
