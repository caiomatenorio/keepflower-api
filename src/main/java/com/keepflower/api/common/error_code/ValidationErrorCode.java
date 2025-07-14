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
    VE001("validation.required"),
    VE002A("validation.username.characters"),
    VE002B("validation.username.min-length"),
    VE002C("validation.username.max-length");

    private final String messageKey;

    public String getMessage(MessageSource messageSource, @Nullable Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    public static ValidationErrorCode valueOfOrDefault(String name) {
        return valueOfOrDefault(name, VE000);
    }

    public static ValidationErrorCode valueOfOrDefault(String name, ValidationErrorCode defaultValue) {
        return Stream.of(values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElse(defaultValue);
    }
}
