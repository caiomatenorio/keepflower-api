package com.keepflower.api.common.util;

import com.keepflower.api.common.error_code.CodeWithMessageKey;
import com.keepflower.api.common.error_code.ValidationErrorCode;
import com.keepflower.api.common.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ErrorMessageUtil {
    private final MessageSource messageSource;

    public String getCodeMessage(CodeWithMessageKey code, @Nullable Object... args) {
        return messageSource.getMessage(code.getMessageKey(), args, LocaleContextHolder.getLocale());
    }

    public String getCodeMessage(CodeWithMessageKey code) {
        Object[] args = code.getMessageArgs();
        return getCodeMessage(code, args);
    }

    public Map<String, List<ValidationError>> formatValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.collectingAndThen(
                                Collectors.mapping(fieldError -> {
                                    String errorCodeStr = fieldError.getDefaultMessage();

                                    if (errorCodeStr == null || errorCodeStr.equals("*")) { // "*" hides the error
                                        return null;
                                    }

                                    ValidationErrorCode errorCode = ValidationErrorCode.valueOfOrDefault(errorCodeStr);
                                    String message = getCodeMessage(errorCode, fieldError.getArguments());
                                    return new ValidationError(message, errorCode);
                                }, Collectors.filtering(Objects::nonNull, Collectors.toList())), // Filter out nulls
                                validationErrors -> validationErrors.stream()
                                        .sorted(Comparator.comparing(ValidationError::errorCode)) // Sort by error code
                                        .toList()
                        )
                ));
    }
}
