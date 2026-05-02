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
public class DishShortInfoDto {

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Name")
    private String name;
}