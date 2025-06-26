package com.keepflower.api.common.error_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ValidationErrorCode {
    VE000("validation.unknown"),
    VE001("validation.required");


    private final String messageKey;

    public static Optional<ValidationErrorCode> optionalOf(String name) {
        return Stream.of(values()).filter(v -> v.name().equals(name)).findFirst();
    }

    public String getMessage(MessageSource messageSource, Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    public static ValidationErrorCode valueOfOrDefault(String name) {
        return valueOfOrDefault(name, VE000);
    }

    public static ValidationErrorCode valueOfOrDefault(String name, ValidationErrorCode defaultValue) {
        return optionalOf(name).orElse(defaultValue);
    }
}
