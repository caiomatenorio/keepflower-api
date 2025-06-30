package com.keepflower.api.common.exception;

import com.keepflower.api.common.error_code.ErrorCode;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException() {
        super(ErrorCode.E001);
    }
}
