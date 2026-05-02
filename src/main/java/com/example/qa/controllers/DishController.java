package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.enums.*;
import com.example.qa.services.DishService;
import com.example.qa.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DishController {

    final DishService dishService;

    @PostMapping
    public ResponseEntity<CreateEntityResponse> createEntity(@RequestBody @Valid CreateDishRequest request){
        return dishService.createEntity(request);
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getEntities(
            @RequestParam(name = "category", required = false) DishCategory category,
            @RequestParam(name = "flags", required = false) List<Flag> flags,
            @RequestParam(name = "search", required = false) String search
    ) {
        var result = dishService.getEntities(category, flags, search);
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getEntity(@PathVariable(name = "id") UUID request){
        return dishService.getEntity(request);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ChangeEntityResponse> changeEntity(
            @PathVariable UUID id,
            @RequestBody @Valid ChangeDishRequest request){
        return dishService.changeEntity(id, request);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(@PathVariable(name = "id") UUID request){
        return dishService.deleteEntity(request);
    }
}