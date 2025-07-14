package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;

public class UsernameAlreadyInUseException extends HttpException {
    public UsernameAlreadyInUseException() {
        super(ErrorCode.E003);
    }
}
