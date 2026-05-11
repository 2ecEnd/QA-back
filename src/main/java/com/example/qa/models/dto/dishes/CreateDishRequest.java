package com.example.qa.models.dto.dishes;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
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
public class CreateDishRequest {

    @JsonProperty(value = "Name")
    @Size(min = 2)
    public String name;

    @JsonProperty(value = "Photos")
    @Nullable
    @Size(max = 5)
    public List<String> photos;

    @JsonProperty(value = "CalorieContent")
    @DecimalMin("0")
    public Double calorieContent;

    @JsonProperty(value = "Proteins")
    @DecimalMin("0")
    public Double proteins;

    @JsonProperty(value = "Fats")
    @DecimalMin("0")
    public Double fats;

    @JsonProperty(value = "Carbohydrates")
    @DecimalMin("0")
    public Double carbohydrates;

    @JsonProperty(value = "Composition")
    public List<Ingredient> composition;

    @JsonProperty(value = "Size")
    @DecimalMin("0")
    public Double size;

    @JsonProperty(value = "Category")
    public DishCategory category;

    @JsonProperty(value = "Flags")
    @Nullable
    public Set<Flag> flags;
}