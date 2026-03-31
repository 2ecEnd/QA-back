package com.example.qa.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteEntityResponse {
    @JsonProperty(value = "Count")
    public Integer count;
}