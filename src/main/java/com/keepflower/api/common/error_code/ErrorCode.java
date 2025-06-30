package com.keepflower.api.common.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    E000("error.unknown", 500),
    E001("error.unauthorized", 401);

    private final String messageKey;
    private final int statusCode;

    public String getMessage(MessageSource messageSource, @Nullable Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
