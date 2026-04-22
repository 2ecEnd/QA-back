package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.CookingNecessity;
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
@CrossOrigin(origins = "*")
public class ProductController {

    final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createEntity(@RequestBody @Valid CreateProductRequest request){
        return productService.createEntity(request);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getEntities(
            @RequestParam(name = "category", required = false) ProductCategory category,
            @RequestParam(name = "readinessDegree", required = false) CookingNecessity cookingNecessity,
            @RequestParam(name = "flags", required = false) List<Flag> flags,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) SortField sort
    ){
        return productService.getEntities(category,
                cookingNecessity,
                flags,
                search,
                sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getEntity(@PathVariable(name = "id") UUID id){
        return productService.getEntity(id);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ChangeEntityResponse> changeEntity(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid ChangeProductRequest request){
        return productService.changeEntity(id, request);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(@PathVariable(name = "id") UUID id){
        return productService.deleteEntity(id);
    }
}