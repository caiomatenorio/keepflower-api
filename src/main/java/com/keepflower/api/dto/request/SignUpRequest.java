package com.keepflower.api.dto.request;

import com.keepflower.api.common.validation.annotation.ValidPassword;
import com.keepflower.api.common.validation.annotation.ValidUsername;

public record SignUpRequest(
        @ValidUsername String username,
        @ValidPassword String password) {
}
