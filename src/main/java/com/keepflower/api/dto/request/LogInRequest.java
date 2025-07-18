package com.keepflower.api.dto.request;

import com.keepflower.api.common.validation.annotation.Required;

public record LogInRequest(
		@Required String username,
		@Required String password) {
}
