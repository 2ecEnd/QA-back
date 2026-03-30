package com.example.qa.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteEntityResponse {
    @JsonProperty(value = "Count")
    public Integer count;
}