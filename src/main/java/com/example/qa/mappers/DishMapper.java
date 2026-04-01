package com.example.qa.mappers;

import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingridient;
import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.DishProduct;
import com.example.qa.repositories.DishRepository;
import com.example.qa.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DishMapper {

    final ProductRepository productRepository;

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

    public Dish toEntity(DishDto dish) {
        var entity = Dish.builder()
                .name(dish.name)
                .photos(dish.photos)
                .calorieContent(dish.calorieContent)
                .proteins(dish.proteins)
                .fats(dish.fats)
                .carbohydrates(dish.carbohydrates)
                .composition(new ArrayList<>())
                .size(dish.size)
                .category(dish.category)
                .flags(dish.flags == null ? null : dish.flags)
                .build();

        dish.composition.forEach(ingridient -> {
            entity.composition.add(
                    DishProduct.builder()
                    .product(productRepository.findById(ingridient.productId).get())
                    .dish(entity)
                    .amount(ingridient.amount)
                    .build()
            );
        });

        return entity;
    }

    public Dish toEntity(CreateDishRequest dish) {
        var entity = Dish.builder()
                .name(dish.name)
                .photos(dish.photos)
                .calorieContent(dish.calorieContent)
                .proteins(dish.proteins)
                .fats(dish.fats)
                .carbohydrates(dish.carbohydrates)
                .composition(new ArrayList<>())
                .size(dish.size)
                .category(dish.category)
                .flags(dish.flags == null ? null : dish.flags)
                .build();

        dish.composition.forEach(ingridient -> {
            entity.composition.add(
                    DishProduct.builder()
                            .product(productRepository.findById(ingridient.productId).get())
                            .dish(entity)
                            .amount(ingridient.amount)
                            .build()
            );
        });

        return entity;
    }

    public Dish toEntity(ChangeDishRequest dish) {
        var entity = Dish.builder()
                .name(dish.name)
                .photos(dish.photos)
                .calorieContent(dish.calorieContent)
                .proteins(dish.proteins)
                .fats(dish.fats)
                .carbohydrates(dish.carbohydrates)
                .composition(new ArrayList<>())
                .size(dish.size)
                .category(dish.category)
                .flags(dish.flags == null ? null : dish.flags)
                .build();

        dish.composition.forEach(ingridient -> {
            entity.composition.add(
                    DishProduct.builder()
                            .product(productRepository.findById(ingridient.productId).get())
                            .dish(entity)
                            .amount(ingridient.amount)
                            .build()
            );
        });

        return entity;
    }
}