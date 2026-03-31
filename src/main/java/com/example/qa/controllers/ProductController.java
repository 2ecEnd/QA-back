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
import jakarta.validation.Valid;
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
    public ResponseEntity<CreateEntityResponse> createEntity(@RequestBody @Valid CreateProductRequest request){
        return productService.createEntity(request);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getEntities(
            @PathVariable(name = "category", required = false) ProductCategory category,
            @PathVariable(name = "readinessDegree", required = false) ReadinessDegree readinessDegree,
            @PathVariable(name = "flags", required = false) List<Flag> flags,
            @PathVariable(name = "search", required = false) String search,
            @PathVariable(name = "sort", required = false) SortField sort
    ){
        return productService.getEntities(category,
                readinessDegree,
                flags,
                search,
                sort);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getEntity(@RequestParam(name = "id") UUID request){
        return productService.getEntity(request);
    }

    @PutMapping("/change")
    public ResponseEntity<ChangeEntityResponse> changeEntity(@RequestBody @Valid ChangeProductRequest request){
        return productService.changeEntity(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(@RequestParam(name = "id") UUID request){
        return productService.deleteEntity(request);
    }
}