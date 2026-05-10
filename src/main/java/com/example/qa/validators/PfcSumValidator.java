package com.example.qa.validators;

import com.example.qa.annotations.ValidPfcSum;
import com.example.qa.models.dto.products.CreateProductRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PfcSumValidator implements ConstraintValidator<ValidPfcSum, CreateProductRequest> {

    @Override
    public boolean isValid(CreateProductRequest dto, ConstraintValidatorContext context) {
        if (dto == null) return false;

        double proteins = dto.getProteins() != null ? dto.getProteins() : 0;
        double fats = dto.getFats() != null ? dto.getFats() : 0;
        double carbs = dto.getCarbohydrates() != null ? dto.getCarbohydrates() : 0;

        return (proteins + fats + carbs) <= 100.0;
    }
}