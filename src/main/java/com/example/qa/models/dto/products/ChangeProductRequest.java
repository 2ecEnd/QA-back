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
public class ChangeProductRequest {

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
    @Nullable
    public String composition;

    @JsonProperty(value = "Category")
    public ProductCategory category;

    @JsonProperty(value = "CookingNecessity")
    public CookingNecessity cookingNecessity;

    @JsonProperty(value = "Flags")
    @Nullable
    public Set<Flag> flags;
}