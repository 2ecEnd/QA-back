package com.example.qa.models.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeEntityResponse {
    @JsonProperty(value = "Count")
    public Integer count;
}