package com.example.qa.DishApiTest;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("POST /dishes/create")
class DishApiCreateEntityTest extends BaseDishApiTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @DisplayName("POST /dishes/create valid cpfc creates new product")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("positiveCpfBoundaries")
    void createEntityPositiveDataCreatesEntity(double cal, double prot, double fat, double carb) {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Салат")
                .composition(List.of(
                        new Ingredient(productA, "Product1", 150f)
                ))
                .size(150.0)
                .category(DishCategory.SALAD)
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> resp = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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


    @DisplayName("POST /dishes/create invalid cpfc throws error")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("negativeCpfBoundaries")
    void createEntityNegativeDataThrowsError(double cal, double prot, double fat, double carb) {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Салат")
                .composition(List.of(
                        new Ingredient(productA, "Product1", 150f)
                ))
                .size(150.0)
                .category(DishCategory.SALAD)
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> resp = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
                Arguments.of(0, 0, 0, -0.00001)
        );
    }


    @DisplayName("POST /dishes/create valid size creates new product")
    @ParameterizedTest(name = "{index} => size={0}")
    @ValueSource(doubles = {0, 1, 0.1, 0.001, 0.00000001, 100000})
    void createEntityPositiveSizeThrowsError(double size) {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Bad size")
                .composition(List.of(new Ingredient(productA, "Product1", 100f)))
                .size(size)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .build();

        ResponseEntity<CreateEntityResponse> resp = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


    @DisplayName("POST /dishes/create invalid size creates new product")
    @ParameterizedTest(name = "{index} => size={0}")
    @ValueSource(doubles = {-1, -0.1, -0.001, -0.00000001, -100000})
    void createEntityNegativeSizeThrowsError(double size) {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Bad size")
                .composition(List.of(new Ingredient(productA, "Product1", 100f)))
                .size(size)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @DisplayName("POST /dishes/create non exists product throws error")
    @Test
    void createEntityNonexistsProductThrowsError() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Bad size")
                .composition(List.of(new Ingredient(UUID.randomUUID(), "aboba", 100f)))
                .size(100.0)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @DisplayName("POST /dishes/create with flag that product doesnt have sets empty flags")
    @Test
    void createEntityWithFlagProductsDoesntHaveSetsEmptyFlags() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Dish1")
                .composition(List.of(new Ingredient(productA, "Product1", 100f)))
                .size(100.0)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .flags(Set.of(Flag.GLUTEN_FREE, Flag.VEGAN, Flag.SUGAR_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );
        DishDto dish = getDishById(response.getBody().getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(dish.getFlags().size()).isEqualTo(1);
    }


    @DisplayName("POST /dishes/create short name throws error")
    @Test
    void createEntityShortNameThrowsError() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("A")
                .composition(List.of(new Ingredient(UUID.randomUUID(), "aboba", 100f)))
                .size(100.0)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @DisplayName("POST /dishes/create too many photos throws error")
    @Test
    void createEntityToManyPhotosThrowsError() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Dish1")
                .photos(List.of("a", "a", "a", "a", "a", "a"))
                .composition(List.of(new Ingredient(UUID.randomUUID(), "aboba", 100f)))
                .size(100.0)
                .category(DishCategory.SECOND)
                .calorieContent(100.0)
                .proteins(10.0)
                .fats(5.0)
                .carbohydrates(85.0)
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/dishes"),
                request,
                CreateEntityResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}