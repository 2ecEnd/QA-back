package com.example.qa.models.dto.products;

import com.example.qa.annotations.PfcSum;
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
@PfcSum
public class CreateProductRequest {

    @JsonProperty(value = "Name")
    public String name;

    @JsonProperty(value = "Photos")
    public List<String> photos;

    @JsonProperty(value = "CalorieContent")
    public Float calorieContent;

    @JsonProperty(value = "Proteins")
    public Float proteins;

    @JsonProperty(value = "Fats")
    public Float fats;

    @JsonProperty(value = "Carbohydrates")
    public Float carbohydrates;

    @JsonProperty(value = "Composition")
    @Nullable
    public String composition;

    @JsonProperty(value = "Category")
    public ProductCategory category;

    @JsonProperty(value = "CookingNecessity")
    public CookingNecessity cookingNecessity;

    @JsonProperty(value = "Flags")
    public Set<Flag> flags;
}