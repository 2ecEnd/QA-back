package com.example.qa.models.dto.dishes;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishDto {

    @JsonProperty(value = "Id")
    private UUID id;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Photos")
    private List<String> photos;

    @JsonProperty(value = "CalorieContent")
    @DecimalMin("0")
    private Double calorieContent;

    @JsonProperty(value = "Proteins")
    @DecimalMin("0")
    private Double proteins;

    @JsonProperty(value = "Fats")
    @DecimalMin("0")
    private Double fats;

    @JsonProperty(value = "Carbohydrates")
    @DecimalMin("0")
    private Double carbohydrates;

    @JsonProperty(value = "Composition")
    private List<Ingredient> composition;

    @JsonProperty(value = "Size")
    private Double size;

    @JsonProperty(value = "Category")
    private DishCategory category;

    @JsonProperty(value = "Flags")
    private Set<Flag> flags;

    @JsonProperty(value = "CreationDate")
    private LocalDateTime creationDate;

    @JsonProperty(value = "EditDate")
    @Nullable
    private LocalDateTime editDate;
}