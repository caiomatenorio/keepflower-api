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
    E002("error.validation", 400);

    private final String messageKey;
    private final int statusCode;

    /**
     * Retrieves the message corresponding to this error code using the provided
     * MessageSource and the current locale. This method does not use any
     * additional arguments for message formatting.
     *
     * @param messageSource the MessageSource to use for message retrieval
     * @return the localized message string
     */
    public String getMessage(MessageSource messageSource) {
        return getMessage(messageSource, (Object) null);
    }

    /**
     * Retrieves the message corresponding to this error code using the provided
     * MessageSource, the current locale, and the given message formatting
     * arguments.
     *
     * @param messageSource the MessageSource to use for message retrieval
     * @param args          the arguments to use for message formatting
     * @return the localized message string
     */
    public String getMessage(MessageSource messageSource, @Nullable Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
