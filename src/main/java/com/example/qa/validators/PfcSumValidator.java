package com.example.qa.validators;

import com.example.qa.annotations.ValidPfcSum;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PfcSumValidator implements ConstraintValidator<ValidPfcSum, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        double proteins = 100, fats = 100, carbs = 100;

        if (value instanceof CreateProductRequest req) {
            proteins = req.getProteins() != null ? req.getProteins() : 0;
            fats = req.getFats() != null ? req.getFats() : 0;
            carbs = req.getCarbohydrates() != null ? req.getCarbohydrates() : 0;
        } else if (value instanceof ChangeProductRequest req) {
            proteins = req.getProteins() != null ? req.getProteins() : 0;
            fats = req.getFats() != null ? req.getFats() : 0;
            carbs = req.getCarbohydrates() != null ? req.getCarbohydrates() : 0;
        }
        return (proteins + fats + carbs) <= 100.0;
    }
}