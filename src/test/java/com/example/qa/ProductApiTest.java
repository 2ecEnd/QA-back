package com.example.qa;

import com.example.qa.BaseApiTest;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.*;
import com.example.qa.models.enums.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProductController API")
class ProductApiTest extends BaseApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Создание продукта с валидными данными
    @Test
    @DisplayName("POST /products/create – создать продукт")
    void createProduct() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Курица")
                .calorieContent(110.0)
                .proteins(23.0)
                .fats(1.0)
                .carbohydrates(0.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .flags(Set.of(Flag.GLUTEN_FREE))
                .build();

        ResponseEntity<CreateEntityResponse> response = restTemplate.postForEntity(
                url("/products/create"), request, CreateEntityResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    // Граничные значения БЖУ – параметризованный тест
    @ParameterizedTest(name = "{index} => prot={0}, fat={1}, carb={2}")
    @MethodSource("bjuBoundaries")
    void createProductBjuValidation(double prot, double fat, double carb) {
        CreateProductRequest req = CreateProductRequest.builder()
                .name("Boundary")
                .calorieContent(100.0)
                .proteins(prot)
                .fats(fat)
                .carbohydrates(carb)
                .category(ProductCategory.FOOD)
                .cookingNecessity(CookingNecessity.READY)
                .build();

        ResponseEntity<String> resp = restTemplate.postForEntity(url("/products/create"), req, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    static Stream<Arguments> bjuBoundaries() {
        return Stream.of(
                Arguments.of(0.0, 0.0, 100.0),
                Arguments.of(99.9, 0.1, 0.0),
                Arguments.of(33, 33, 33)
        );
    }

    /*
    // Получение списка продуктов с фильтрами и поиском
    @Test
    void getProducts() {
        // Создадим два продукта
        CreateProductRequest req1 = CreateProductRequest.builder()
                .name("Банан")
                .calorieContent(89.0)
                .proteins(1.1)
                .fats(0.3)
                .carbohydrates(22.8)
                .category(ProductCategory.SWEETS)
                .cookingNecessity(CookingNecessity.READY)
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
                .build();
        restTemplate.postForEntity(url("/products/create"), req2, CreateEntityResponse.class);

        // Поиск по подстроке "гов"
        ResponseEntity<String> rawResp = restTemplate.getForEntity(
                url("/products?search=гов"), String.class);
        System.out.println("Raw response: " + rawResp.getBody());

        ResponseEntity<ProductDto[]> response = restTemplate.getForEntity(
                url("/products?search=гов"), ProductDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Говядина");

        // Фильтр по категории
        ResponseEntity<ProductDto[]> sweetsResp = restTemplate.getForEntity(
                url("/products?category=SWEETS"), ProductDto[].class);
        assertThat(sweetsResp.getBody()).hasSize(1);
        assertThat(sweetsResp.getBody()[0].getCategory()).isEqualTo(ProductCategory.SWEETS);
    }

    // Получение продукта по ID
    @Test
    void getProductById() {
        CreateEntityResponse created = restTemplate.postForObject(url("/products/create"),
                CreateProductRequest.builder()
                        .name("ID test")
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
        assertThat(resp.getBody().getName()).isEqualTo("ID test");
    }

    // Обновление продукта
    @Test
    void updateProduct() {
        CreateEntityResponse created = restTemplate.postForObject(url("/products/create"),
                CreateProductRequest.builder()
                        .name("Update")
                        .calorieContent(10.0)
                        .proteins(1.0)
                        .fats(0.5)
                        .carbohydrates(0.0)
                        .category(ProductCategory.FOOD)
                        .cookingNecessity(CookingNecessity.READY)
                        .build(),
                CreateEntityResponse.class);

        ChangeProductRequest change = ChangeProductRequest.builder()
                .name("Updated")
                .calorieContent(20.0)
                .proteins(2.0)
                .fats(1.0)
                .carbohydrates(0.0)
                .category(ProductCategory.MEAT)
                .cookingNecessity(CookingNecessity.RAW)
                .build();
        ResponseEntity<ChangeEntityResponse> patchResp = restTemplate.exchange(
                url("/products/" + created.getId() + "/update"), HttpMethod.PATCH,
                new HttpEntity<>(change), ChangeEntityResponse.class);

        assertThat(patchResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(patchResp.getBody().getCount()).isEqualTo(1);
    }

    // Удаление продукта (успешное и конфликт при использовании в блюде)
    @Nested
    @DisplayName("DELETE /products/{id}/delete")
    class DeleteProduct {
        private ProductDto freeProduct;
        private ProductDto usedProduct;

        @BeforeEach
        void setUp() {
            freeProduct = createProduct("Free product", 50,5,2,3);
            usedProduct = createProduct("Used ingredient", 100,10,5,80);
            // Создаём блюдо, куда входит usedProduct
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

        @Test
        void deleteFreeProductReturns200() {
            ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.getForEntity(
                    url("/products/" + freeProduct.getId() + "/delete"), DeleteProductAcknowledge.class);
            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resp.getBody().getAcknowledge()).isTrue();
        }

        @Test
        void deleteUsedProductReturns409WithDishList() {
            ResponseEntity<DeleteProductAcknowledge> resp = restTemplate.getForEntity(
                    url("/products/" + usedProduct.getId() + "/delete"), DeleteProductAcknowledge.class);
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
    */
}