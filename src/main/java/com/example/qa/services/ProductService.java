package com.example.qa.services;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ResponseEntity<CreateEntityResponse> createEntity(CreateProductRequest request);

    ResponseEntity<List<ProductDto>> getEntities();

    ResponseEntity<ProductDto> getEntity(UUID request);

    ResponseEntity<ChangeEntityResponse> changeEntity(ChangeProductRequest request);

    ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request);
}