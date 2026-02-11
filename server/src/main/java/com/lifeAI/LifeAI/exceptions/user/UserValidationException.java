package com.lifeAI.LifeAI.exceptions.user;

import com.lifeAI.LifeAI.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class UserValidationException extends ValidationException {
    public UserValidationException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
