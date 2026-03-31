package com.example.qa.models.dto.dishes;

import com.example.qa.annotations.PfcSum;
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
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PfcSum
public class ChangeDishRequest {

    @JsonProperty(value = "Id")
    public UUID id;

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
    public List<Ingridient> composition;

    @JsonProperty(value = "Size")
    public Float size;

    @JsonProperty(value = "Category")
    public DishCategory category;

    @JsonProperty(value = "Flags")
    @Nullable
    public Set<Flag> flags;
}