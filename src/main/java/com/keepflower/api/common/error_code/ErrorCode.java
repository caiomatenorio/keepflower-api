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
    E001("error.unauthorized", 401),
    E002("error.validation", 400),
    E003("error.username_already_in_use", 409);

    private final String messageKey;
    private final int statusCode;

    public String getMessage(MessageSource messageSource) {
        return getMessage(messageSource, (Object) null);
    }

    public String getMessage(MessageSource messageSource, @Nullable Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
