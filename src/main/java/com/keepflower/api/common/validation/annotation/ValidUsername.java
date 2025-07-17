package com.keepflower.api.common.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Custom annotation for validating usernames. The username must:
 * <ul>
 *     <li>Contain only alphanumeric characters and underscores</li>
 *     <li>Be between 3 and 32 characters long</li>
 * </ul>
 */
@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "VE002A")
@Size(min = 3, max = 32, message = "VE002B")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "*"; // Hidden in response messages

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
