package com.example.qa.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateEntityResponse {

    @JsonProperty(value = "Id")
    public UUID id;
}