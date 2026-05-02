package com.example.qa.services;

import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;

import java.util.List;
import java.util.UUID;

public interface DishService {

    UUID createEntity(CreateDishRequest request);

    List<DishDto> getEntities(
            DishCategory category,
            List<Flag> flags,
            String search
    );

    DishDto getEntity(UUID request);

    Integer changeEntity(UUID id, ChangeDishRequest request);

    DeleteDishAcknowledge deleteEntity(UUID id);
}