package com.example.qa.models.dto.dishes;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeDishRequest {

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Photos")
    private List<String> photos;

    @JsonProperty(value = "CalorieContent")
    private Double calorieContent;

    @JsonProperty(value = "Proteins")
    private Double proteins;

    @JsonProperty(value = "Fats")
    private Double fats;

    @JsonProperty(value = "Carbohydrates")
    private Double carbohydrates;

    @JsonProperty(value = "Composition")
    private List<Ingredient> composition;

    @JsonProperty(value = "Size")
    private Double size;

    @JsonProperty(value = "Category")
    private DishCategory category;

    @JsonProperty(value = "Flags")
    @Nullable
    private Set<Flag> flags;
}