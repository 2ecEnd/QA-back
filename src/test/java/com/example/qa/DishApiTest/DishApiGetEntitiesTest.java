package com.example.qa.DishApiTest;


import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.dishes.DishDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET /dishes")
class DishApiGetEntitiesTest extends BaseDishApiTest {

    @BeforeEach
    void addDishes() {
        createDish(
                "Салат",
                100.0,
                "SALAD",
                100.0,
                10.0,
                5.0,
                75.0,
                List.of(Map.entry(productA, 100f)),
                Set.of("VEGAN", "GLUTEN_FREE"),
                null
        );
        createDish(
                "Борщ",
                200.0,
                "SOUP",
                100.0,
                10.0,
                5.0,
                85.0,
                List.of(Map.entry(productB, 100f)),
                Set.of("GLUTEN_FREE", "SUGAR_FREE"),
                null
        );
    }


    @DisplayName("GET /dishes returns correct dishes list")
    @Test
    void getEntitiesReturnsCorrectDishesList() {
        ResponseEntity<DishDto[]> response = restTemplate.getForEntity(
                url("/dishes"),
                DishDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @DisplayName("GET /dishes with category returns correct dishes list")
    @Test
    void getEntitiesWithCategoryReturnsCorrectDishesList() {
        ResponseEntity<DishDto[]> response = restTemplate.getForEntity(
                url("/dishes?category=SALAD"),
                DishDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Салат");
    }

    @DisplayName("GET /dishes with flags returns correct dishes list")
    @Test
    void getEntitiesWithFlagsReturnsCorrectDishesList() {
        ResponseEntity<DishDto[]> response = restTemplate.getForEntity(
                url("/dishes?flags=VEGAN&flags=GLUTEN_FREE"),
                DishDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Салат");
    }

    @DisplayName("GET /dishes with search returns correct dishes list")
    @Test
    void getEntitiesWithSearchReturnsCorrectDishesList() {
        ResponseEntity<DishDto[]> response = restTemplate.getForEntity(
                url("/dishes?search=бор"),
                DishDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Борщ");
    }
}