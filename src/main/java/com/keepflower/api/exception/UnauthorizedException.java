package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException() {
        super(ErrorCode.E001);
    }
}
