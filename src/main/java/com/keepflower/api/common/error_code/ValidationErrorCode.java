package com.keepflower.api.common.error_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ValidationErrorCode {
    VE000("validation.unknown"),
    VE001("validation.required");

    private final String messageKey;

    /**
     * Retrieves the message corresponding to this error code using the provided
     * MessageSource and the current locale. The provided arguments are used for
     * message formatting.
     *
     * @param messageSource the MessageSource to use for message retrieval
     * @param args          arguments to use for message formatting
     * @return the localized message string
     */
    public String getMessage(MessageSource messageSource, @Nullable Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    /**
     * Finds the {@link ValidationErrorCode} that matches the given name, or
     * returns the default value ({@link #VE000}) if no match is found.
     *
     * @param name the name of the error code to look up
     * @return the error code matching the given name, or the default
     */
    public static ValidationErrorCode valueOfOrDefault(String name) {
        return valueOfOrDefault(name, VE000);
    }

    /**
     * Finds the {@link ValidationErrorCode} that matches the given name, or
     * returns the provided default value if no match is found.
     *
     * @param name         the name of the error code to look up
     * @param defaultValue the default error code to return if no match is found
     * @return the error code matching the given name, or the provided default
     */
    public static ValidationErrorCode valueOfOrDefault(String name, ValidationErrorCode defaultValue) {
        return Stream.of(values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElse(defaultValue);
    }
}
