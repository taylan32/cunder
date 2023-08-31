package com.example.cunder.controller.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {ChangePasswordRequestValidator.class})
@Target({METHOD, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangePasswordRequestConstraint {
    String message() default "New password is not confirmed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
