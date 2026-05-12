package com.example.qa.DishApiTest;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("PUT /dishes/{id}/update")
class DishApiChangeEntityTest extends BaseDishApiTest {

    private UUID created;
    private ChangeDishRequest change;

    @BeforeEach
    void addDishes() {
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
        change = ChangeDishRequest.builder()
                .name("Updated")
                .photos(List.of("aboba"))
                .calorieContent(20.0)
                .proteins(2.0)
                .fats(1.0)
                .carbohydrates(1.5)
                .composition(List.of(
                        new Ingredient(productB, "Product2", 150f)
                ))
                .size(300.0)
                .category(DishCategory.FIRST)
                .flags(Set.of(Flag.GLUTEN_FREE, Flag.SUGAR_FREE))
                .build();
    }


    @DisplayName("PUT /dishes/{id}/update valid data changes dish")
    @Test
    void changeEntityValidDataChangesDish() {
        ResponseEntity<ChangeEntityResponse> putResp =
                restTemplate.exchange(
                        url("/dishes/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(change), ChangeEntityResponse.class
                );

        DishDto dish = getDishById(created);

        assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(dish.getName()).isEqualTo("Updated");
        assertThat(dish.getPhotos()).hasSize(1);
        assertThat((dish.getPhotos()).get(0)).isEqualTo("aboba");
        assertThat(dish.getCalorieContent()).isCloseTo(20.0, Percentage.withPercentage(1));
        assertThat(dish.getProteins()).isCloseTo(2.0, Percentage.withPercentage(1));
        assertThat(dish.getFats()).isCloseTo(1.0, Percentage.withPercentage(1));
        assertThat(dish.getComposition()).hasSize(1);
        assertThat((dish.getComposition().get(0)).getProductId()).isEqualTo(productB);
        assertThat(dish.getCarbohydrates()).isCloseTo(1.5, Percentage.withPercentage(1));
        assertThat(dish.getCategory()).isEqualTo(DishCategory.FIRST);
        assertThat(dish.getFlags()).isEqualTo(new HashSet<>());
    }


    @DisplayName("PUT /dishes/{id}/update valid cpfc changes dish")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("positiveCpfBoundaries")
    void changeEntityValidCpfcChangesDish(double cal, double prot, double fat, double carb) {
        change.setCalorieContent(cal);
        change.setProteins(prot);
        change.setFats(fat);
        change.setCarbohydrates(carb);

        ResponseEntity<ChangeEntityResponse> putReps =
                restTemplate.exchange(
                        url("/dishes/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(change), ChangeEntityResponse.class
                );

        DishDto dish = getDishById(created);

        assertThat(putReps.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(dish.getCalorieContent()).isCloseTo(cal, Percentage.withPercentage(1));
        assertThat(dish.getProteins()).isCloseTo(prot, Percentage.withPercentage(1));
        assertThat(dish.getFats()).isCloseTo(fat, Percentage.withPercentage(1));
        assertThat(dish.getCarbohydrates()).isCloseTo(carb, Percentage.withPercentage(1));
    }

    static Stream<Arguments> positiveCpfBoundaries() {
        return Stream.of(
                Arguments.of(0.0, 0.0, 0.0, 0.0),
                Arguments.of(1.0, 0.0, 0.0, 0.0),
                Arguments.of(0.0, 1.0, 0.0, 0.0),
                Arguments.of(0.0, 0.0, 1.0, 0.0),
                Arguments.of(0.0, 0.0, 0.0, 1.0),
                Arguments.of(0, 0, 0, 0),
                Arguments.of(1, 0, 0, 0),
                Arguments.of(0, 1, 0, 0),
                Arguments.of(0, 0, 1, 0),
                Arguments.of(0, 0, 0, 1),
                Arguments.of(0.1, 0, 0, 0),
                Arguments.of(0, 0.1, 0, 0),
                Arguments.of(0, 0, 0.1, 0),
                Arguments.of(0, 0, 0, 0.1),
                Arguments.of(0.00001, 0, 0, 0),
                Arguments.of(0, 0.00001, 0, 0),
                Arguments.of(0, 0, 0.00001, 0),
                Arguments.of(0, 0, 0, 0.00001)
        );
    }


    @DisplayName("PUT /dishes/{id}/update valid cpfc throws error")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("negativeCpfBoundaries")
    void changeEntityInvalidCpfcThrowsError(double cal, double prot, double fat, double carb) {
        change.setCalorieContent(cal);
        change.setProteins(prot);
        change.setFats(fat);
        change.setCarbohydrates(carb);

        ResponseEntity<ChangeEntityResponse> response =
                restTemplate.exchange(
                        url("/dishes/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(change), ChangeEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<Arguments> negativeCpfBoundaries() {
        return Stream.of(
                Arguments.of(-1, 0, 0, 0),
                Arguments.of(0, -1, 0, 0),
                Arguments.of(0, 0, -1, 0),
                Arguments.of(0, 0, 0, -1),
                Arguments.of(-0.1, 0, 0, 0),
                Arguments.of(0, -0.1, 0, 0),
                Arguments.of(0, 0, -0.1, 0),
                Arguments.of(0, 0, 0, -0.1),
                Arguments.of(-0.00001, 0, 0, 0),
                Arguments.of(0, -0.00001, 0, 0),
                Arguments.of(0, 0, -0.00001, 0),
                Arguments.of(0, 0, 0, -0.00001),
                Arguments.of(-100000, 0, 0, 0),
                Arguments.of(0, -100000, 0, 0),
                Arguments.of(0, 0.0, -100000, 0.0),
                Arguments.of(0, 0.0, 0.0, -100000)
        );
    }
}