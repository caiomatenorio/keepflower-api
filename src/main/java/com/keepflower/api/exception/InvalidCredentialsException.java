package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;

public class InvalidCredentialsException extends HttpException {
    public InvalidCredentialsException() {
        super(ErrorCode.E004);
    }
}
