package com.example.qa.mappers;

import com.example.qa.models.dto.dishes.*;
import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.DishProduct;
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
                .id(dish.getId())
                .name(dish.getName())
                .photos(dish.getPhotos())
                .calorieContent(dish.getCalorieContent())
                .proteins(dish.getProteins())
                .fats(dish.getFats())
                .carbohydrates(dish.getCarbohydrates())
                .composition(dish.getComposition().stream()
                        .map(obj -> Ingredient.builder()
                                .productId(obj.product.getId())
                                .productName(obj.product.getName())
                                .amount(obj.amount)
                                .build())
                        .toList()
                )
                .size(dish.getSize())
                .category(dish.getCategory())
                .flags(dish.getFlags())
                .creationDate(dish.getCreatedAt())
                .editDate(dish.getUpdatedAt())
                .build();
    }

    public Dish toEntity(DishDto dish) {
        var entity = Dish.builder()
                .name(dish.getName())
                .photos(dish.getPhotos())
                .calorieContent(dish.getCalorieContent())
                .proteins(dish.getProteins())
                .fats(dish.getFats())
                .carbohydrates(dish.getCarbohydrates())
                .composition(new ArrayList<>())
                .size(dish.getSize())
                .category(dish.getCategory())
                .flags(dish.getFlags() == null ? null : dish.getFlags())
                .build();

        for (var ingredient : dish.getComposition()) {
            entity.getComposition().add(
                    DishProduct.builder()
                            .product(productRepository.findById(ingredient.productId).get())
                            .dish(entity)
                            .amount(ingredient.amount)
                            .build()
            );
        }

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
            entity.getComposition().add(
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
                .name(dish.getName())
                .photos(dish.getPhotos())
                .calorieContent(dish.getCalorieContent())
                .proteins(dish.getProteins())
                .fats(dish.getFats())
                .carbohydrates(dish.getCarbohydrates())
                .composition(new ArrayList<>())
                .size(dish.getSize())
                .category(dish.getCategory())
                .flags(dish.getFlags() == null ? null : dish.getFlags())
                .build();

        dish.getComposition().forEach(ingridient -> {
            entity.getComposition().add(
                    DishProduct.builder()
                            .product(productRepository.findById(ingridient.productId).get())
                            .dish(entity)
                            .amount(ingridient.amount)
                            .build()
            );
        });

        return entity;
    }

    public DishShortInfoDto toShortInfo(Dish dish) {
        return DishShortInfoDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .build();
    }
}