package com.example.qa;

import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.*;
import com.example.qa.models.enums.*;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DishController API")
class DishApiTest extends BaseApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID productA;
    private UUID productB;

    @BeforeEach
    void createIngredients() {
        productA = createProduct(
                "Product1",
                100,
                10,
                5,
                75,
                "SWEETS",
                "READY",
                Set.of("SUGAR_FREE"),
                new ArrayList<>()
        );
        productB = createProduct(
                "Product2",
                200,
                20,
                10,
                60,
                "MEAT",
                "RAW",
                new HashSet<>(),
                new ArrayList<>()
        );
    }


    @Nested
    @DisplayName("POST /dishes/create")
    class CreateEntity {

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
            ResponseEntity<String> resp = restTemplate.postForEntity(url("/dishes"), request, String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
            ResponseEntity<String> resp = restTemplate.postForEntity(url("/dishes"), request, String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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
            ResponseEntity<CreateEntityResponse> resp = restTemplate.postForEntity(
                    url("/dishes"),
                    request,
                    CreateEntityResponse.class
            );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(resp.getBody()).isNotNull();
            UUID dishId = resp.getBody().getId();

            Integer flagCount = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM dish_flags WHERE dish_id = ?",
                    Integer.class,
                    dishId
            );
            assertThat(flagCount).isEqualTo(1);
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
/*
        @DisplayName("POST /dishes/create non exists product throws error")
        @Test
        void createEntityWithMacrosDeletesAllMacros() {
            CreateDishRequest request = CreateDishRequest.builder()
                    .name("Dish1!десерт!салат")
                    .composition(List.of(new Ingredient(UUID.randomUUID(), "aboba", 100f)))
                    .size(100.0)
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
            assertThat(resp.getBody()).isNotNull();
            UUID dishId = resp.getBody().getId();

            Integer flagCount = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM dish_flags WHERE dish_id = ?",
                    Integer.class,
                    dishId.toString()
            );
            assertThat(flagCount).isEqualTo(1);
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
            ResponseEntity<String> resp = restTemplate.postForEntity(url("/dishes"), request, String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
        */
    }


    @Nested
    @DisplayName("GET /dishes")
    class GetEntities {

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
                    List.of(Map.entry(productA, 100f)),
                    Set.of("GLUTEN_FREE", "SUGAR_FREE"),
                    null
            );
        }


        @DisplayName("GET /dishes returns correct dishes list")
        @Test
        void getEntitiesReturnsCorrectDishesList() {
            ResponseEntity<String> rawResp = restTemplate.getForEntity(
                    url("/dishes"), String.class);
            System.out.println("Raw response: " + rawResp.getBody());

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


    @Nested
    @DisplayName("GET /dishes/{id}")
    class GetEntity {

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
            ResponseEntity<DishDto> resp = restTemplate.getForEntity(
                    url("/dishes/" + created),
                    DishDto.class);
            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(resp.getBody().getName()).isEqualTo("Test dish");
        }

        @Test
        void getDishByIdNonexistsIdThrowsError() {
            ResponseEntity<DishDto> resp = restTemplate.getForEntity(
                    url("/dishes/" + UUID.randomUUID().toString()),
                    DishDto.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }


    @Nested
    @DisplayName("PUT /dishes/{id}/update")
    class ChangeEntity {
        UUID created;
        ChangeDishRequest change;

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

            ResponseEntity<DishDto> dish =
                    restTemplate.getForEntity(url("/dishes/" + created), DishDto.class);
            DishDto respBody = dish.getBody();

            assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respBody.getName()).isEqualTo("Updated");
            assertThat(respBody.getPhotos()).hasSize(1);
            assertThat((respBody.getPhotos()).get(0)).isEqualTo("aboba");
            assertThat(respBody.getCalorieContent()).isCloseTo(20.0, Percentage.withPercentage(1));
            assertThat(respBody.getProteins()).isCloseTo(2.0, Percentage.withPercentage(1));
            assertThat(respBody.getFats()).isCloseTo(1.0, Percentage.withPercentage(1));
            assertThat(respBody.getComposition()).hasSize(1);
            assertThat((respBody.getComposition().get(0)).getProductId()).isEqualTo(productB);
            assertThat(respBody.getCarbohydrates()).isCloseTo(1.5, Percentage.withPercentage(1));
            assertThat(respBody.getCategory()).isEqualTo(DishCategory.FIRST);
            assertThat(respBody.getFlags()).isEqualTo(new HashSet<>());
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

            ResponseEntity<DishDto> dish =
                    restTemplate.getForEntity(url("/dishes/" + created), DishDto.class);
            DishDto respBody = dish.getBody();

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


    @Nested
    @DisplayName("DELETE /dishes/{id}/delete")
    class DeleteEntity {
        UUID created;

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
            assertThat(resp.getBody().getAcknowledge()).isTrue();
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
            assertThat(resp.getBody().getAcknowledge()).isFalse();
        }
    }


    private UUID createProduct(
            String name,
            double calorieContent,
            double proteins,
            double fats,
            double carbohydrates,
            String category,
            String cookingNecessity,
            Set<String> flags,
            List<String> photos
    ) {
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO products (id, name, calorie_content, proteins, fats, carbohydrates, " +
                        "category, cooking_necessity, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?)",
                id, name, calorieContent, proteins, fats, carbohydrates,
                category, cookingNecessity, LocalDateTime.now(), null);

        if (flags != null) {
            for (String flag : flags) {
                jdbc.update("INSERT INTO product_flags (product_id, flags) VALUES (?,?)", id, flag);
            }
        }
        if (photos != null) {
            for (String photo : photos) {
                jdbc.update("INSERT INTO product_photos (product_id, photos) VALUES (?,?)", id, photo);
            }
        }

        return id;
    }

    private UUID createDish(
            String name,
            double size,
            String category,
            double calorieContent,
            double proteins,
            double fats,
            double carbohydrates,
            List<Map.Entry<UUID, Float>> ingredients, // productId -> amount
            Set<String> flags,
            List<String> photos
    ) {
        UUID dishId = UUID.randomUUID();
        jdbc.update("INSERT INTO dishes (id, name, size, category, calorie_content, proteins, fats, " +
                        "carbohydrates, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?)",
                dishId, name, size, category, calorieContent, proteins, fats,
                carbohydrates, LocalDateTime.now(), null);

        if (flags != null) {
            for (String flag : flags) {
                jdbc.update("INSERT INTO dish_flags (dish_id, flags) VALUES (?,?)", dishId, flag);
            }
        }
        if (photos != null) {
            for (String photo : photos) {
                jdbc.update("INSERT INTO dish_photos (dish_id, photos) VALUES (?,?)", dishId, photo);
            }
        }
        if (ingredients != null) {
            for (Map.Entry<UUID, Float> ing : ingredients) {
                jdbc.update("INSERT INTO dishes_products (id, dish_id, product_id, amount) VALUES (?,?,?,?)",
                        UUID.randomUUID(), dishId, ing.getKey(), ing.getValue());
            }
        }

        return dishId;
    }
}