package com.keepflower.api.common.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.keepflower.api.common.validation.validator.UsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

/**
 * Annotation for validating usernames. Constraints include:
 * <ul>
 * <li>Must contain only alphanumeric characters and underscores</li>
 * <li>Minimum length of 3 characters</li>
 * <li>Maximum length of 32 characters</li>
 * </ul>
 */
@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "VE002A")
@Min(value = 3, message = "VE002B")
@Max(value = 32, message = "VE002C")
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "VE002";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
