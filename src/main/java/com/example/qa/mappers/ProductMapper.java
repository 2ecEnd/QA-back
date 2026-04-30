package com.example.qa.mappers;

import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .photos(product.getPhotos())
                .calorieContent(product.getCalorieContent())
                .proteins(product.getProteins())
                .fats(product.getFats())
                .carbohydrates(product.getCarbohydrates())
                .composition(product.getComposition())
                .category(product.getCategory())
                .cookingNecessity(product.getCookingNecessity())
                .flags(product.getFlags())
                .creationDate(product.getCreatedAt())
                .editDate(product.getUpdatedAt())
                .build();
    }

    public Product toEntity(ProductDto product){
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .photos(product.getPhotos())
                .calorieContent(product.getCalorieContent())
                .proteins(product.getProteins())
                .fats(product.getFats())
                .carbohydrates(product.getCarbohydrates())
                .composition(product.getComposition())
                .category(product.getCategory())
                .cookingNecessity(product.getCookingNecessity())
                .flags(product.getFlags())
                .build();
    }
}