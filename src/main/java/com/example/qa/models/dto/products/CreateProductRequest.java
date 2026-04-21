package com.example.qa.models.dto.products;

import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.CookingNecessity;
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
public class CreateProductRequest {

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
    @Nullable
    private String composition;

    @JsonProperty(value = "Category")
    private ProductCategory category;

    @JsonProperty(value = "CookingNecessity")
    private CookingNecessity cookingNecessity;

    @JsonProperty(value = "Flags")
    private Set<Flag> flags;
}