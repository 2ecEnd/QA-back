package com.example.qa.models.dto.dishes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteDishAcknowledge {
    @JsonProperty(value = "Acknowledge")
    public Boolean acknowledge;
}