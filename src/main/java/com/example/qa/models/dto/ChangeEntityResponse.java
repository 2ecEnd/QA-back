package com.example.qa.models.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangeEntityResponse {
    @JsonProperty(value = "Count")
    public Integer count;
}