package com.keepflower.api.common.util;

import com.keepflower.api.common.error_code.ValidationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ErrorMessageUtil {
    private final MessageSource messageSource;

    public Map<String, List<Map<String, String>>> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(fieldError -> {
                    String defaultMessage = fieldError.getDefaultMessage();

                    if (defaultMessage == null || defaultMessage.equals("*")) {
                        return null;
                    }

                    ValidationErrorCode errorCode = ValidationErrorCode.valueOfOrDefault(defaultMessage);
                    String message = errorCode.getMessage(messageSource, fieldError.getArguments());
                    return Map.of("errorCode", errorCode.name(), "message", message);
                }, Collectors.filtering((Map<String, String> error) -> error != null, Collectors.toList()))));
    }
}
