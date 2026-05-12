package com.example.qa.ProductApiTest;


import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET /products/create")
class ProductApiGetEntitiesTest extends BaseApiTest {

    @BeforeEach
    void addProducts() {
        CreateProductRequest req1 = CreateProductRequest.builder()
                .name("Банан")
                .calorieContent(89.0)
                .proteins(1.1)
                .fats(0.3)
                .carbohydrates(22.8)
                .category(ProductCategory.SWEETS)
                .cookingNecessity(CookingNecessity.READY)
                .flags(Set.of(Flag.GLUTEN_FREE, Flag.VEGAN))
                .build();
        restTemplate.postForEntity(url("/products/create"), req1, CreateEntityResponse.class);

        CreateProductRequest req2 = CreateProductRequest.builder()
                .name("Говядина")
                .calorieContent(250.0)
                .proteins(26.0)
                .fats(15.0)
                .carbohydrates(0.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE, Flag.SUGAR_FREE))
                .build();
        restTemplate.postForEntity(url("/products/create"), req2, CreateEntityResponse.class);
    }


    @DisplayName("GET /products returns correct products list")
    @Test
    void getEntitiesReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @DisplayName("GET /products with category returns correct products list")
    @Test
    void getEntitiesCategoryReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?category=SWEETS"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
    }

    @DisplayName("GET /products with cooking necessity returns correct products list")
    @Test
    void getEntitiesCookingNecessityReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?readinessDegree=RAW"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
    }

    @DisplayName("GET /products with flag returns correct products list")
    @Test
    void getEntitiesFlagReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?flags=VEGAN"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
    }

    @DisplayName("GET /products with few flags returns correct products list")
    @Test
    void getEntitiesFewFlagsReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?flags=GLUTEN_FREE&flags=SUGAR_FREE"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
    }

    @DisplayName("GET /products with filters returns correct products list")
    @Test
    void getEntitiesFilterReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?search=гов"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
    }

    @DisplayName("GET /products with sorting returns correct products list")
    @Test
    void getEntitiesSortReturnsCorrectProductList() {
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(
                        url("/products?sort=NAME"),
                        ProductDto[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
        assertThat(response.getBody()[1].getName()).isEqualTo("Говядина");
    }
}