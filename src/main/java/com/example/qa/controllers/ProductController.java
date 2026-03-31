package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.ReadinessDegree;
import com.example.qa.models.enums.SortField;
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
        return productService.createEntity(request);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getEntities(
            @PathVariable(name = "category", required = false) ProductCategory category,
            @PathVariable(name = "category", required = false) ReadinessDegree readinessDegree,
            @PathVariable(name = "category", required = false) List<Flag> flags,
            @PathVariable(name = "category", required = false) String name,
            @PathVariable(name = "category", required = false) SortField sort
            ){
        return productService.getEntities(category,
                readinessDegree,
                flags,
                name,
                sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getEntity(UUID request){
        return productService.getEntity(request);
    }

    @PutMapping("/change")
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeProductRequest request){
        return productService.changeEntity(request);
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request){
        return productService.deleteEntity(request);
    }
}