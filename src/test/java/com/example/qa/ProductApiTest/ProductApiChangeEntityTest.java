package com.example.qa.ProductApiTest;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
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


@DisplayName("PUT /products/{id}/update")
class ProductApiChangeEntityTest extends BaseApiTest {
    private UUID created;

    @BeforeEach
    void addProduct() {
        created = restTemplate.postForObject(url("/products/create"),
                CreateProductRequest.builder()
                        .name("Update")
                        .calorieContent(10.0)
                        .proteins(1.0)
                        .fats(0.5)
                        .carbohydrates(0.0)
                        .category(ProductCategory.FOOD)
                        .cookingNecessity(CookingNecessity.READY)
                        .flags(Set.of(Flag.GLUTEN_FREE))
                        .build(),
                CreateEntityResponse.class)
                .getId();
    }


    @DisplayName("PUT /products/{id}/update valid data changes product")
    @Test
    void changeEntityValidDataChangesProduct() {
        ChangeProductRequest change = ChangeProductRequest.builder()
                .name("Updated")
                .photos(List.of("aboba"))
                .calorieContent(20.0)
                .proteins(2.0)
                .fats(1.0)
                .carbohydrates(1.5)
                .composition("aboba2")
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.VEGAN, Flag.SUGAR_FREE))
                .build();

        ResponseEntity<ChangeEntityResponse> response =
                restTemplate.exchange(
                        url("/products/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(change), ChangeEntityResponse.class
                );
        ProductDto product = getProductById(created);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(product.getName()).isEqualTo("Updated");
        assertThat(product.getPhotos()).hasSize(1);
        assertThat((product.getPhotos()).get(0)).isEqualTo("aboba");
        assertThat(product.getCalorieContent()).isCloseTo(20.0, Percentage.withPercentage(1));
        assertThat(product.getProteins()).isCloseTo(2.0, Percentage.withPercentage(1));
        assertThat(product.getFats()).isCloseTo(1.0, Percentage.withPercentage(1));
        assertThat(product.getComposition()).isEqualTo("aboba2");
        assertThat(product.getCarbohydrates()).isCloseTo(1.5, Percentage.withPercentage(1));
        assertThat(product.getCategory()).isEqualTo(ProductCategory.MEAT);
        assertThat(product.getCookingNecessity()).isEqualTo(CookingNecessity.RAW);
        assertThat(product.getFlags()).isEqualTo(Set.of(Flag.VEGAN, Flag.SUGAR_FREE));
    }


    @DisplayName("PUT /products/{id}/update valid cpfc changes product")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("positiveCpfBoundaries")
    void changeEntityValidCpfcChangesProduct(double cal, double prot, double fat, double carb) {
        ChangeProductRequest request = ChangeProductRequest.builder()
                .name("Update")
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .category(ProductCategory.FOOD)
                .cookingNecessity(CookingNecessity.READY)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();
        ResponseEntity<ChangeEntityResponse> putReps =
                restTemplate.exchange(
                        url("/products/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(request), ChangeEntityResponse.class
                );

        ProductDto product = getProductById(created);

        assertThat(putReps.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(product.getCalorieContent()).isCloseTo(cal, Percentage.withPercentage(1));
        assertThat(product.getProteins()).isCloseTo(prot, Percentage.withPercentage(1));
        assertThat(product.getFats()).isCloseTo(fat, Percentage.withPercentage(1));
        assertThat(product.getCarbohydrates()).isCloseTo(carb, Percentage.withPercentage(1));
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


    @DisplayName("PUT /products/{id}/update valid cpfc throws error")
    @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
    @MethodSource("negativeCpfBoundaries")
    void changeEntityInvalidCpfcThrowsError(double cal, double prot, double fat, double carb) {
        ChangeProductRequest request = ChangeProductRequest.builder()
                .name("Product")
                .calorieContent(cal)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<ChangeEntityResponse> response =
                restTemplate.exchange(
                        url("/products/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(request), ChangeEntityResponse.class
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
    void changeEntityShortNameThrowsError() {
        ChangeProductRequest request = ChangeProductRequest.builder()
                .name("A")
                .calorieContent(1.0)
                .proteins(1.0)
                .fats(1.0)
                .carbohydrates(1.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<ChangeEntityResponse> response =
                restTemplate.exchange(
                        url("/products/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(request), ChangeEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @DisplayName("POST /products/create too many photos throws error")
    @Test
    void changeEntityToManyPhotosThrowsError() {
        ChangeProductRequest request = ChangeProductRequest.builder()
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

        ResponseEntity<ChangeEntityResponse> response =
                restTemplate.exchange(
                        url("/products/" + created + "/update"),
                        HttpMethod.PUT,
                        new HttpEntity<>(request), ChangeEntityResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}