package com.example.qa.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateEntityResponse {

    @JsonProperty(value = "Id")
    public UUID id;
}