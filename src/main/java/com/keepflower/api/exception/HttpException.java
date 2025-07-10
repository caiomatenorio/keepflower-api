package com.keepflower.api.exception;

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

    /**
     * Returns the localized error message for the error code associated with this
     * exception, using the given message source.
     *
     * @param messageSource the message source to use for message retrieval
     * @return the localized error message
     */
    public String getErrorMessage(MessageSource messageSource) {
        return errorCode.getMessage(messageSource, (Object) null);
    }
}
