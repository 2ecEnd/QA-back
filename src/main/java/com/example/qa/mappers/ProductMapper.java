package com.example.qa.mappers;

import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product){
        return ProductDto.builder()
                .id(product.id)
                .name(product.name)
                .photos(product.photos)
                .calorieContent(product.calorieContent)
                .proteins(product.proteins)
                .fats(product.fats)
                .carbohydrates(product.carbohydrates)
                .composition(product.composition)
                .category(product.category)
                .readinessDegree(product.readinessDegree)
                .flags(product.flags)
                .build();
    }

    public Product toEntity(ProductDto product){
        return Product.builder()
                .id(product.id)
                .name(product.name)
                .photos(product.photos)
                .calorieContent(product.calorieContent)
                .proteins(product.proteins)
                .fats(product.fats)
                .carbohydrates(product.carbohydrates)
                .composition(product.composition)
                .category(product.category)
                .readinessDegree(product.readinessDegree)
                .flags(product.flags)
                .build();
    }
}