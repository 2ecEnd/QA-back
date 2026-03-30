package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createProduct(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(){
        return ResponseEntity.internalServerError().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<ChangeEntityResponse> editProduct(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteProduct(){
        return ResponseEntity.internalServerError().build();
    }
}