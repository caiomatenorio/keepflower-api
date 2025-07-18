package com.keepflower.api.common.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking fields or parameters as required.
 * This annotation is used to ensure that the annotated field or parameter is not null.
 * It can be applied to fields and parameters in request DTOs.
 */
@NotNull(message = "VE001")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
    String message() default "*"; // Hidden in response messages

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
