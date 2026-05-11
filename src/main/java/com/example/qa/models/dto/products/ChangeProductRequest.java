package com.example.qa.models.dto.products;

import com.example.qa.annotations.ValidPfcSum;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.CookingNecessity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMax;
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
@ValidPfcSum
public class ChangeProductRequest {

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
    @DecimalMax("100")
    public Double proteins;

    @JsonProperty(value = "Fats")
    @DecimalMin("0")
    @DecimalMax("100")
    public Double fats;

    @JsonProperty(value = "Carbohydrates")
    @DecimalMin("0")
    @DecimalMax("100")
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