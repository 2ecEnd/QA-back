package com.example.qa.models.dto.dishes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @JsonProperty(value = "ProductId")
    public UUID productId;

    @JsonProperty(value = "ProductName")
    public String productName;

    @JsonProperty(value = "Amount")
    public Float amount;
}