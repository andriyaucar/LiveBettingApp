package org.bilyoner.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.bilyoner.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidationUtil<T> {

    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public void validateMatchDto(T dto) {
        Set<ConstraintViolation<T>> errors = validator.validate(dto);

        if (!errors.isEmpty()) {

            StringBuilder errorMessage = new StringBuilder();

            errors.forEach(error -> {
                errorMessage.append(error.getMessage()).append(" ");
            });

            throw new CustomException(errorMessage.toString());
        }
    }
}
