package com.example.qa.services;

import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.DeleteProductAcknowledge;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.SortField;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    UUID createEntity(CreateProductRequest request);

    List<ProductDto> getEntities(
            ProductCategory category,
            CookingNecessity cookingNecessity,
            List<Flag> flags,
            String name,
            SortField sort);

    ProductDto getEntity(UUID id);

    Integer changeEntity(UUID id, ChangeProductRequest request);

    DeleteProductAcknowledge deleteEntity(UUID id);
}