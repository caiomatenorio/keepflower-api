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
 * Custom annotation for validating passwords.
 * The password must:
 * <ul>
 *     <li>Contain at least one uppercase letter</li>
 *     <li>Contain at least one lowercase letter</li>
 *     <li>Contain at least one digit</li>
 *     <li>Contain at least one of the following special characters: !@#$%^&*()_+-=[]{};':"\|,.<>/?`~</li>
 *     <li>Contain only alphanumeric and the following special characters: !@#$%^&*()_+-=[]{};':"\|,.<>/?`~</li>
 *     <li>Be between 8 and 32 characters long</li>
 * </ul>
 */
@Pattern(regexp = "^(?=.*[A-Z]).*$", message = "VE003A")
@Pattern(regexp = "^(?=.*[a-z]).*$", message = "VE003B")
@Pattern(regexp = "^(?=.*\\d).*$", message = "VE003C")
@Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~]).*$", message = "VE003D")
@Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~]+$", message = "VE003E")
@Size(min = 8, max = 32, message = "VE003F")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "*"; // Hidden in response messages

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
