package com.keepflower.api.common.validation;

import com.keepflower.api.common.error_code.ValidationErrorCode;

public record ValidationError(String message, ValidationErrorCode errorCode) {
}
