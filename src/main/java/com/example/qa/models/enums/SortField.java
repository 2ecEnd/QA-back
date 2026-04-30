package com.example.qa.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortField {
    NAME("name"),
    CALORIE_CONTENT("calorieContent"),
    PROTEINS("proteins"),
    FATS("fats"),
    CARBOHYDRATES("carbohydrates");

    private final String fieldName;
}