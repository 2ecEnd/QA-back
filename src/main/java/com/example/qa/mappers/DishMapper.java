package com.example.qa.mappers;

import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingridient;
import com.example.qa.models.entities.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    public DishDto toDto(Dish dish) {
        return DishDto.builder()
                .name(dish.name)
                .photos(dish.photos)
                .calorieContent(dish.calorieContent)
                .proteins(dish.proteins)
                .fats(dish.fats)
                .carbohydrates(dish.carbohydrates)
                .composition(dish.composition.stream()
                        .map(obj -> Ingridient.builder()
                                .productId(obj.product.id)
                                .productName(obj.product.name)
                                .amount(obj.amount)
                                .build())
                        .toList()
                )
                .size(dish.size)
                .category(dish.category)
                .flags(dish.flags)
                .creationDate(dish.creationDate)
                .editDate(dish.editDate)
                .build();
    }

    /*public Dish toEntity(DishDto dish) {
        return Dish.builder()
                .name(dish.name)
                .photos(dish.photos)
                .calorieContent(dish.calorieContent)
                .proteins(dish.proteins)
                .fats(dish.fats)
                .carbohydrates(dish.carbohydrates)
                .composition(dish.composition.stream()
                        .map(obj -> DishProduct.builder()
                                .dish(obj.)
                                .
                                .amount(obj.amount)
                                .build())
                        .toList()
                )
                .size(dish.size)
                .category(dish.category)
                .flags(dish.flags)
                .creationDate(dish.creationDate)
                .editDate(dish.editDate)
                .build();
    }*/
}