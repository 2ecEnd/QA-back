package com.example.qa.annotations;

import com.example.qa.validators.PfcSumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PfcSumValidator.class)
public @interface ValidPfcSum {
    String message() default "Сумма БЖУ не может превышать 100 г";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}