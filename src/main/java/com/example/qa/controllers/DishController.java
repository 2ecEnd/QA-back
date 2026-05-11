package com.example.qa.controllers;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.enums.*;
import com.example.qa.services.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        UUID dishId = dishService.createEntity(request);

        return dishId == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.created(URI.create("/dishes/" + dishId.toString()))
                .body(new CreateEntityResponse(dishId));
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getEntities(
            @RequestParam(name = "category", required = false) DishCategory category,
            @RequestParam(name = "flags", required = false) List<Flag> flags,
            @RequestParam(name = "search", required = false) String search
    ){
        List<DishDto> result = dishService.getEntities(category, flags, search);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getEntity(@PathVariable(name = "id") UUID id){
        DishDto dish = dishService.getEntity(id);

        return dish == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(dish);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ChangeEntityResponse> changeEntity(
            @PathVariable UUID id,
            @RequestBody @Valid ChangeDishRequest request
    ){
        Integer changedCount = dishService.changeEntity(id, request);

        return changedCount == 0 ?
                ResponseEntity.status(404).body(new ChangeEntityResponse(changedCount)) :
                ResponseEntity.ok(new ChangeEntityResponse(changedCount)) ;
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DeleteDishAcknowledge> deleteEntity(@PathVariable(name = "id") UUID id){
        DeleteDishAcknowledge acknowledge = dishService.deleteEntity(id);

        return acknowledge.getAcknowledge() ?
                ResponseEntity.ok(acknowledge) :
                ResponseEntity.status(404).body(acknowledge);
    }
}