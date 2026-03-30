package com.example.qa.services;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<CreateEntityResponse> createEntity();

    ResponseEntity<List<ProductDto>> getEntities();

    ResponseEntity<ProductDto> getEntity();

    ResponseEntity<ChangeEntityResponse> editEntity();

    ResponseEntity<DeleteEntityResponse> deleteEntity();
}