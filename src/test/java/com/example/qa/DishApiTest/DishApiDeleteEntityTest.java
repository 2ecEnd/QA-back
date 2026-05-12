package com.example.qa.DishApiTest;


import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DELETE /dishes/{id}/delete")
class DishApiDeleteEntityTest extends BaseDishApiTest {

    private UUID created;

    @BeforeEach
    void addDish() {
        created = createDish(
                "Update",
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


    @DisplayName("DELETE /dishes/{id}/delete exists id deletes dish")
    @Test
    void deleteEntityExistsIdDeletesDish() {
        ResponseEntity<DeleteDishAcknowledge> resp = restTemplate.exchange(
                url("/dishes/" + created + "/delete"),
                HttpMethod.DELETE,
                null,
                DeleteDishAcknowledge.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("DELETE /dishes/{id}/delete non exists id throws error")
    @Test
    void deleteEntityNonexistsIdThrowsError() {
        ResponseEntity<DeleteDishAcknowledge> resp = restTemplate.exchange(
                url("/dishes/" + UUID.randomUUID() + "/delete"),
                HttpMethod.DELETE,
                null,
                DeleteDishAcknowledge.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}