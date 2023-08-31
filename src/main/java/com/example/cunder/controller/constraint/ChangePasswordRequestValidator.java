package com.example.cunder.controller.constraint;

import com.example.cunder.dto.user.ChangePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordRequestValidator implements ConstraintValidator<ChangePasswordRequestConstraint, ChangePasswordRequest> {


    @Override
    public void initialize(ChangePasswordRequestConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(ChangePasswordRequest value, ConstraintValidatorContext context) {
        return value.newPassword().equals(value.newPasswordConfirm());
    }
}
