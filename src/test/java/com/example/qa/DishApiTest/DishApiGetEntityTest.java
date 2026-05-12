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

@DisplayName("GET /dishes/{id}")
class DishApiGetEntityTest extends BaseDishApiTest {

    private UUID created;

    @BeforeEach
    void addDish() {
        created = createDish(
                "Test dish",
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
    }


    @DisplayName("GET /dishes/{id}")
    @Test
    void getDishByIdExistsIdReturnsDish() {
        ResponseEntity<DishDto> resp =
                restTemplate.getForEntity(
                        url("/dishes/" + created),
                        DishDto.class
                );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getDishByIdNonexistsIdThrowsError() {
        ResponseEntity<DishDto> resp =
                restTemplate.getForEntity(
                        url("/dishes/" + UUID.randomUUID()),
                        DishDto.class
                );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}