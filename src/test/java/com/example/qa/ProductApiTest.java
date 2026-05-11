package com.example.qa;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.*;
import com.example.qa.models.enums.*;
import lombok.RequiredArgsConstructor;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProductController API")
class ProductApiTest extends BaseApiTest {
    @Autowired
    private TestRestTemplate restTemplate;


    @Nested
    @DisplayName("POST /products/create")
    class CreateEntity {

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
                    restTemplate.postForEntity(url("/products/create"), request, CreateEntityResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isNotNull();
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
                    restTemplate.postForEntity(url("/products/create"), request, CreateEntityResponse.class);

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
    }

    @Nested
    @DisplayName("GET /products/create")
    class GetEntities {

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
                    restTemplate.getForEntity(url("/products"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
        }

        @DisplayName("GET /products with category returns correct products list")
        @Test
        void getEntitiesCategoryReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?category=SWEETS"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
        }

        @DisplayName("GET /products with cooking necessity returns correct products list")
        @Test
        void getEntitiesCookingNecessityReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?readinessDegree=RAW"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
        }

        @DisplayName("GET /products with flag returns correct products list")
        @Test
        void getEntitiesFlagReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?flags=VEGAN"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
        }

        @DisplayName("GET /products with few flags returns correct products list")
        @Test
        void getEntitiesFewFlagsReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?flags=GLUTEN_FREE&flags=SUGAR_FREE"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
        }

        @DisplayName("GET /products with filters returns correct products list")
        @Test
        void getEntitiesFilterReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?search=гов"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");
        }

        @DisplayName("GET /products with sorting returns correct products list")
        @Test
        void getEntitiesSortReturnsCorrectProductList() {
            ResponseEntity<ProductDto[]> response =
                    restTemplate.getForEntity(url("/products?sort=NAME"), ProductDto[].class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody()[0].getName()).isEqualTo("Банан");
            assertThat(response.getBody()[1].getName()).isEqualTo("Говядина");
        }
    }

    @Nested
    @DisplayName("GET /products/{id}")
    class GetEntity {

        @DisplayName("GET /products/{id} exist id returns product")
        @Test
        void getEntityExistIdReturnsProduct() {
            CreateEntityResponse created = restTemplate.postForObject(url("/products/create"),
                    CreateProductRequest.builder()
                            .name("Product")
                            .calorieContent(50.0)
                            .proteins(5.0)
                            .fats(1.0)
                            .carbohydrates(2.0)
                            .category(ProductCategory.FOOD)
                            .cookingNecessity(CookingNecessity.READY)
                            .build(),
                    CreateEntityResponse.class);

            ResponseEntity<ProductDto> resp = restTemplate.getForEntity(
                    url("/products/" + created.getId()), ProductDto.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resp.getBody().getName()).isEqualTo("Product");
        }

        @DisplayName("GET /products/{id} throws error")
        @Test
        void getEntityNonExistIdThrowsError() {
            ResponseEntity<ProductDto> resp = restTemplate.getForEntity(
                    url("/products/" + UUID.randomUUID()), ProductDto.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("PUT /products/{id}/update")
    class ChangeEntity {
        CreateEntityResponse created;

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
                    CreateEntityResponse.class);
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
            ResponseEntity<ChangeEntityResponse> putResp =
                    restTemplate.exchange(
                            url("/products/" + created.getId() + "/update"),
                            HttpMethod.PUT,
                            new HttpEntity<>(change), ChangeEntityResponse.class
                    );

            ResponseEntity<ProductDto> product =
                    restTemplate.getForEntity(url("/products/" + created.getId()), ProductDto.class);
            ProductDto respBody = product.getBody();

            assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respBody.getName()).isEqualTo("Updated");
            assertThat(respBody.getPhotos()).hasSize(1);
            assertThat((respBody.getPhotos()).get(0)).isEqualTo("aboba");
            assertThat(respBody.getCalorieContent()).isCloseTo(20.0, Percentage.withPercentage(1));
            assertThat(respBody.getProteins()).isCloseTo(2.0, Percentage.withPercentage(1));
            assertThat(respBody.getFats()).isCloseTo(1.0, Percentage.withPercentage(1));
            assertThat(respBody.getComposition()).isEqualTo("aboba2");
            assertThat(respBody.getCarbohydrates()).isCloseTo(1.5, Percentage.withPercentage(1));
            assertThat(respBody.getCategory()).isEqualTo(ProductCategory.MEAT);
            assertThat(respBody.getCookingNecessity()).isEqualTo(CookingNecessity.RAW);
            assertThat(respBody.getFlags()).isEqualTo(Set.of(Flag.VEGAN, Flag.SUGAR_FREE));
        }


        @DisplayName("PUT /products/{id}/update valid cpfc changes product")
        @ParameterizedTest(name = "{index} => cal={0}, prot={1}, fat={2}, carb={3}")
        @MethodSource("positiveCpfBoundaries")
        void createEntityValidCpfcChangesProduct(double cal, double prot, double fat, double carb) {
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
                            url("/products/" + created.getId() + "/update"),
                            HttpMethod.PUT,
                            new HttpEntity<>(request), ChangeEntityResponse.class
                    );

            ResponseEntity<ProductDto> product =
                    restTemplate.getForEntity(url("/products/" + created.getId()), ProductDto.class);
            ProductDto respBody = product.getBody();

            assertThat(putReps.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respBody.getCalorieContent()).isCloseTo(cal, Percentage.withPercentage(1));
            assertThat(respBody.getProteins()).isCloseTo(prot, Percentage.withPercentage(1));
            assertThat(respBody.getFats()).isCloseTo(fat, Percentage.withPercentage(1));
            assertThat(respBody.getCarbohydrates()).isCloseTo(carb, Percentage.withPercentage(1));
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
        void createEntityInvalidCpfcThrowsError(double cal, double prot, double fat, double carb) {
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
                            url("/products/" + created.getId() + "/update"),
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
    }

    @Nested
    @DisplayName("DELETE /products/{id}/delete")
    class DeleteProduct {
        private ProductDto freeProduct;
        private ProductDto usedProduct;

        @BeforeEach
        void setUp() {
            freeProduct = createProduct("Free product", 50,5,2,3);
            usedProduct = createProduct("Used ingredient", 100,10,5,80);

            CreateDishRequest dishReq = CreateDishRequest.builder()
                    .name("Dish with ingredient")
                    .size(200.0)
                    .category(DishCategory.SECOND)
                    .composition(List.of(
                            Ingredient.builder()
                                    .productId(usedProduct.getId())
                                    .amount(100f)
                                    .build())
                    )
                    .calorieContent(100.0)
                    .proteins(10.0)
                    .fats(5.0)
                    .carbohydrates(80.0)
                    .build();
            restTemplate.postForEntity(url("/dishes"), dishReq, CreateEntityResponse.class);
        }

        @DisplayName("DELETE /products/{id}/delete with free product returns 200")
        @Test
        void deleteEntityFreeProductReturns200() {
            ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.getForEntity(
                    url("/products/" + freeProduct.getId() + "/delete"),
                    DeleteProductAcknowledge.class
            );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resp.getBody().getAcknowledge()).isTrue();
        }

        @DisplayName("DELETE /products/{id}/delete used product returns 409")
        @Test
        void deleteEntityUsedProductReturns409WithDishList() {
            ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.getForEntity(
                        url("/products/" + usedProduct.getId() + "/delete"),
                        DeleteProductAcknowledge.class
                    );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(resp.getBody().getAcknowledge()).isFalse();
            assertThat(resp.getBody().getDishes()).isNotEmpty();
            assertThat(resp.getBody().getDishes().get(0).getName()).isEqualTo("Dish with ingredient");
        }

        private ProductDto createProduct(String name, double cal, double p, double f, double c) {
            CreateProductRequest req = CreateProductRequest.builder()
                    .name(name)
                    .calorieContent(cal)
                    .proteins(p)
                    .fats(f)
                    .carbohydrates(c)
                    .category(ProductCategory.FOOD)
                    .cookingNecessity(CookingNecessity.READY)
                    .build();
            CreateEntityResponse created = restTemplate.postForObject(url("/products/create"), req, CreateEntityResponse.class);
            return restTemplate.getForObject(url("/products/" + created.getId()), ProductDto.class);
        }
    }
}