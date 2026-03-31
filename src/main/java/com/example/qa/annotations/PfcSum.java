package com.example.qa.annotations;

import com.example.qa.validators.FoodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Сумма КБЖУ (Калории, белки, жиры, углеводы)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {FoodValidator.class}
)
public @interface PfcSum {
    String message() default "{constraints.PasswordMatches.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}