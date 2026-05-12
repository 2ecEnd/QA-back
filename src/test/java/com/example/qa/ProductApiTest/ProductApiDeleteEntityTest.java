package com.example.qa.ProductApiTest;


import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.DeleteProductAcknowledge;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.DishCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DELETE /products/{id}/delete")
class ProductApiDeleteEntityTest extends BaseApiTest {
    private ProductDto freeProduct;
    private ProductDto usedProduct;

    @BeforeEach
    void setUp() {
        freeProduct = createProduct("Free product", 50,5,2,3);
        usedProduct = createProduct("Used ingredient", 100,10,5,80);

        createDish(
                "Dish with ingredient",
                200.0,
                "SECOND",
                100.0,
                10.0,
                5.0,
                80.0,
                List.of(Map.entry(usedProduct.getId(), (100f))),
                new HashSet<>(),
                new ArrayList<>()
        );
    }

    @DisplayName("DELETE /products/{id}/delete with free product returns 200")
    @Test
    void deleteEntityFreeProductReturns200() {
        ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.exchange(
                url("/products/" + freeProduct.getId() + "/delete"),
                HttpMethod.DELETE,
                null,
                DeleteProductAcknowledge.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getAcknowledge()).isTrue();
    }

    @DisplayName("DELETE /products/{id}/delete used product returns 409")
    @Test
    void deleteEntityUsedProductReturns409WithDishList() {
        ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.exchange(
                url("/products/" + usedProduct.getId() + "/delete"),
                HttpMethod.DELETE,
                null,
                DeleteProductAcknowledge.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resp.getBody().getAcknowledge()).isFalse();
        assertThat(resp.getBody().getDishes()).isNotEmpty();
        assertThat(resp.getBody().getDishes().get(0).getName()).isEqualTo("Dish with ingredient");
    }

}