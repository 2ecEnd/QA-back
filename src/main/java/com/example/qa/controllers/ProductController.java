package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.DeleteProductAcknowledge;
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
        UUID productId = productService.createEntity(request);

        return ResponseEntity.ok(new CreateEntityResponse(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getEntities(
            @RequestParam(name = "category", required = false) ProductCategory category,
            @RequestParam(name = "readinessDegree", required = false) CookingNecessity cookingNecessity,
            @RequestParam(name = "flags", required = false) List<Flag> flags,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) SortField sort
    ){
        List<ProductDto> result = productService.getEntities(category, cookingNecessity, flags, search, sort);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getEntity(@PathVariable(name = "id") UUID id){
        ProductDto product = productService.getEntity(id);

        return product == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(product);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ChangeEntityResponse> changeEntity(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid ChangeProductRequest request
    ){
        Integer changedCount = productService.changeEntity(id, request);

        return changedCount == 0 ?
                ResponseEntity.status(404).body(new ChangeEntityResponse(changedCount)) :
                ResponseEntity.ok(new ChangeEntityResponse(changedCount)) ;
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<DeleteProductAcknowledge> deleteEntity(@PathVariable(name = "id") UUID id){
        DeleteProductAcknowledge acknowledge = productService.deleteEntity(id);

        return acknowledge.getAcknowledge() ?
                ResponseEntity.ok(acknowledge) :
                acknowledge.getDishes() == null ?
                        ResponseEntity.status(404).body(acknowledge) :
                        ResponseEntity.status(409).body(acknowledge);
    }
}