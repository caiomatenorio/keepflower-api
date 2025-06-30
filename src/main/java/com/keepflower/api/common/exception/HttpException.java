package com.keepflower.api.common.exception;

import com.keepflower.api.common.error_code.ErrorCode;
import lombok.Getter;
import org.springframework.context.MessageSource;

@Getter
public class HttpException extends RuntimeException {
    private final int statusCode;
    private final ErrorCode errorCode;

    public HttpException(ErrorCode errorCode) {
        super(errorCode.name());
        this.statusCode = errorCode.getStatusCode();
        this.errorCode = errorCode;
    }

    public String getMessage(MessageSource messageSource) {
        return errorCode.getMessage(messageSource, (Object) null);
    }
}
