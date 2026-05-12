package com.example.qa.ProductApiTest;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET /products/{id}")
class ProductApiGetEntityTest extends BaseApiTest {

    @DisplayName("GET /products/{id} exist id returns product")
    @Test
    void getEntityExistIdReturnsProduct() {
        ProductDto created = createProduct("Product", 50.0, 5.0, 1.0, 2.0);

        ResponseEntity<ProductDto> response =
                restTemplate.getForEntity(
                        url("/products/" + created.getId()),
                        ProductDto.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Product");
    }

    @DisplayName("GET /products/{id} throws error")
    @Test
    void getEntityNonExistIdThrowsError() {
        ResponseEntity<ProductDto> response =
                restTemplate.getForEntity(
                        url("/products/" + UUID.randomUUID()),
                        ProductDto.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}