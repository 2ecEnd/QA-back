package com.example.qa.ProductApiTest;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("POST /products/create")
class ProductApiCreateEntityTest extends BaseApiTest {

    @DisplayName("POST /products/create creates new product")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("positiveCpfBoundaries")
    void createEntityPositiveDataCreatesEntity(double cal, double prot, double fat, double carb) {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Product")
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response =
                restTemplate.postForEntity(
                        url("/products/create"),
                        request,
                        CreateEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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
                Arguments.of(100, 0, 0, 0),
                Arguments.of(0, 100, 0, 0),
                Arguments.of(0, 0, 100, 0),
                Arguments.of(0, 0, 0, 100),
                Arguments.of(0, 99.9, 0, 0),
                Arguments.of(0, 0, 99.9, 0),
                Arguments.of(0, 0, 0, 99.9),
                Arguments.of(0.1, 0, 0, 0),
                Arguments.of(0, 0.1, 0, 0),
                Arguments.of(0, 0, 0.1, 0),
                Arguments.of(0, 0, 0, 0.1),
                Arguments.of(0.00001, 0, 0, 0),
                Arguments.of(0, 0.00001, 0, 0),
                Arguments.of(0, 0, 0.00001, 0),
                Arguments.of(0, 0, 0, 0.00001),
                Arguments.of(100, 1, 2, 3),
                Arguments.of(100, 5.5, 3.4, 12.3)
        );
    }


    @DisplayName("POST /products/create throws error")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("negativeCpfBoundaries")
    void createEntityNegativeDataThrowsError(double cal, double prot, double fat, double carb) {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Product")
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response =
                restTemplate.postForEntity(
                        url("/products/create"),
                        request,
                        CreateEntityResponse.class
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
                Arguments.of(0, 101, 0, 0),
                Arguments.of(0, 0.0, 101, 0.0),
                Arguments.of(0, 0.0, 0.0, 101),
                Arguments.of(0, 100.1, 0, 0),
                Arguments.of(0, 0.0, 100.1, 0.0),
                Arguments.of(0, 0.0, 0.0, 100.1),
                Arguments.of(10, 50, 40, 30),
                Arguments.of(100.0, 33.4, 33.4, 33.4),
                Arguments.of(100.0, 50.5, 29.67, 78.54)
        );
    }


    @DisplayName("POST /products/create short name throws error")
    @Test
    void createEntityShortNameThrowsError() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("A")
                .calorieContent(1.0)
                .proteins(1.0)
                .fats(1.0)
                .carbohydrates(1.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response =
                restTemplate.postForEntity(
                        url("/products/create"),
                        request,
                        CreateEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @DisplayName("POST /products/create too many photos throws error")
    @Test
    void createEntityToManyPhotosThrowsError() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Product1")
                .photos(List.of("a", "a", "a", "a", "a", "a"))
                .calorieContent(1.0)
                .proteins(1.0)
                .fats(1.0)
                .carbohydrates(1.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response =
                restTemplate.postForEntity(
                        url("/products/create"),
                        request,
                        CreateEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}