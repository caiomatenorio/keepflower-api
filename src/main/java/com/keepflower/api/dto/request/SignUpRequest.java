package com.keepflower.api.dto.request;

import com.keepflower.api.common.validation.annotation.Required;
import com.keepflower.api.common.validation.annotation.ValidPassword;
import com.keepflower.api.common.validation.annotation.ValidUsername;

public record SignUpRequest(
        @Required @ValidUsername String username,
        @Required @ValidPassword String password) {
}
