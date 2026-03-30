package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createEntity(CreateProductRequest request){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getEntities(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getEntity(UUID request){
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/change")
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeProductRequest request){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request){
        return ResponseEntity.internalServerError().build();
    }
}