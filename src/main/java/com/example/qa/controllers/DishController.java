package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.dishes.DishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    @PostMapping("/create")
    public ResponseEntity<CreateEntityResponse> createEntity(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getEntities(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getEntity(){
        return ResponseEntity.internalServerError().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<ChangeEntityResponse> editEntity(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<DeleteEntityResponse> deleteEntity(){
        return ResponseEntity.internalServerError().build();
    }
}