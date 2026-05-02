package com.example.qa.models.dto.products;

import com.example.qa.models.dto.dishes.DishShortInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteProductResponse {

    @JsonProperty("Acknowledge")
    private Boolean acknowledge;

    @JsonProperty("Dishes")
    @Nullable
    private List<DishShortInfoDto> dishes;
}