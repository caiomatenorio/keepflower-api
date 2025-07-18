package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;

public class UsernameAlreadyInUseException extends HttpException {
    public UsernameAlreadyInUseException(String username) {
        super(ErrorCode.E003);
        this.getErrorCode().setMessageArgs(username);
    }
}
