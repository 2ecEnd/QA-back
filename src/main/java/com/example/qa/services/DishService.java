package com.example.qa.services;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.products.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DishService {

    ResponseEntity<CreateEntityResponse> createEntity(CreateDishRequest request);

    ResponseEntity<List<DishDto>> getEntities();

    ResponseEntity<DishDto> getEntity(UUID request);

    ResponseEntity<ChangeEntityResponse> changeEntity(ChangeDishRequest request);

    ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request);
}