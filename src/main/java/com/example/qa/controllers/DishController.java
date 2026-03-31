package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
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

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createEntity(CreateDishRequest request){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getEntities(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getEntity(UUID request){
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/change")
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeDishRequest request){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request){
        return ResponseEntity.internalServerError().build();
    }
}