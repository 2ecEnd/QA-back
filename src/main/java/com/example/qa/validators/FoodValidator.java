package com.example.qa.validators;

import com.example.qa.annotations.PfcSum;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FoodValidator implements ConstraintValidator<PfcSum, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object instanceof CreateProductRequest obj) {
            return (obj.proteins + obj.fats + obj.carbohydrates) <= 100;
        }
        else if (object instanceof ChangeProductRequest obj) {
            return (obj.proteins + obj.fats + obj.carbohydrates) <= 100;
        }
        else if (object instanceof CreateDishRequest obj) {
            return (obj.proteins + obj.fats + obj.carbohydrates) <= 100;
        }
        else if (object instanceof ChangeDishRequest obj) {
            return (obj.proteins + obj.fats + obj.carbohydrates) <= 100;
        }

        return false;
    }
}