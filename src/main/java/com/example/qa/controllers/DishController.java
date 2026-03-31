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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    final DishService dishService;

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createEntity(CreateDishRequest request){
        return dishService.createEntity(request);
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getEntities(
            @PathVariable(name = "category", required = false) DishCategory category,
            @PathVariable(name = "flags", required = false) List<Flag> flags,
            @PathVariable(name = "search", required = false) String search
    ) {
        return dishService.getEntities(category, flags, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getEntity(UUID request){
        return dishService.getEntity(request);
    }

    @PutMapping("/change")
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeDishRequest request){
        return dishService.changeEntity(request);
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request){
        return dishService.deleteEntity(request);
    }
}