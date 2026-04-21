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
public class CreateDishRequest {

    @JsonProperty(value = "Name")
    public String name;

    @JsonProperty(value = "Photos")
    public List<String> photos;

    @JsonProperty(value = "CalorieContent")
    public Double calorieContent;

    @JsonProperty(value = "Proteins")
    public Double proteins;

    @JsonProperty(value = "Fats")
    public Double fats;

    @JsonProperty(value = "Carbohydrates")
    public Double carbohydrates;

    @JsonProperty(value = "Composition")
    public List<Ingridient> composition;

    @JsonProperty(value = "Size")
    public Double size;

    @JsonProperty(value = "Category")
    public DishCategory category;

    @JsonProperty(value = "Flags")
    @Nullable
    public Set<Flag> flags;
}