package com.keepflower.api.common.validation.validator;

import com.keepflower.api.common.validation.annotation.ValidUsername;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    /**
     * Regular expression for validating usernames:
     * <ul>
     * <li>Must be 3 to 32 characters long</li>
     * <li>Can contain letters (a-z, A-Z), digits (0-9), and underscores (_)</li>
     * </ul>
     */
    public static final String REGEX = "^[a-zA-Z0-9_]{3,32}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(REGEX);
    }
}
