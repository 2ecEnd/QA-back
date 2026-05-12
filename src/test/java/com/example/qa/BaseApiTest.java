package com.example.qa;

import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.dto.dishes.Ingredient;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public abstract class BaseApiTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected JdbcTemplate jdbc;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("DELETE FROM dish_flags");
        jdbc.execute("DELETE FROM dish_photos");
        jdbc.execute("DELETE FROM dishes_products");
        jdbc.execute("DELETE FROM dishes");
        jdbc.execute("DELETE FROM product_flags");
        jdbc.execute("DELETE FROM product_photos");
        jdbc.execute("DELETE FROM products");
    }

    protected String url(String path) {
        return "http://localhost:" + port + path;
    }

    private UUID extractUUID(Object value) {
        if (value instanceof UUID uuid) {
            return uuid;
        } else if (value instanceof String str) {
            return UUID.fromString(str);
        }
        throw new IllegalArgumentException("Cannot convert to UUID: " + value);
    }

    private Double getDouble(Map<String, Object> row, String column) {
        Object val = row.get(column);
        if (val == null) return null;
        return ((Number) val).doubleValue();
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;
        if (value instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        throw new IllegalArgumentException("Unexpected date type: " + value.getClass());
    }


    protected ProductDto createProduct(String name, double cal, double p, double f, double c) {
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

    protected UUID createProduct(
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

    protected UUID createDish(
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

    protected ProductDto getProductById(UUID id) {
        Map<String, Object> row = jdbc.queryForMap(
                "SELECT id, name, calorie_content, proteins, fats, carbohydrates, " +
                        "composition, category, cooking_necessity, created_at, updated_at " +
                        "FROM products WHERE id = ?", id);

        UUID productId = extractUUID(row.get("id"));

        List<String> photos = jdbc.queryForList(
                "SELECT photos FROM product_photos WHERE product_id = ?", String.class, id);

        List<String> flagStrings = jdbc.queryForList(
                "SELECT flags FROM product_flags WHERE product_id = ?", String.class, id);

        Set<Flag> flags = flagStrings.stream()
                .map(Flag::valueOf)
                .collect(Collectors.toSet());

        return ProductDto.builder()
                .id(productId)
                .name((String) row.get("name"))
                .photos(photos.isEmpty() ? new ArrayList<>() : photos)
                .calorieContent(getDouble(row, "calorie_content"))
                .proteins(getDouble(row, "proteins"))
                .fats(getDouble(row, "fats"))
                .carbohydrates(getDouble(row, "carbohydrates"))
                .composition((String) row.get("composition"))
                .category(ProductCategory.valueOf((String) row.get("category")))
                .cookingNecessity(CookingNecessity.valueOf((String) row.get("cooking_necessity")))
                .flags(flags)
                .creationDate(toLocalDateTime(row.get("created_at")))
                .editDate(toLocalDateTime(row.get("updated_at")))
                .build();
    }

    protected DishDto getDishById(UUID id) {
        Map<String, Object> row = jdbc.queryForMap(
                "SELECT id, name, calorie_content, proteins, fats, carbohydrates, " +
                        "size, category, created_at, updated_at FROM dishes WHERE id = ?", id);

        UUID dishId = extractUUID(row.get("id"));

        List<String> photos = jdbc.queryForList(
                "SELECT photos FROM dish_photos WHERE dish_id = ?", String.class, id);

        List<String> flagStrings = jdbc.queryForList(
                "SELECT flags FROM dish_flags WHERE dish_id = ?", String.class, id);
        Set<Flag> flags = flagStrings.stream()
                .map(Flag::valueOf)
                .collect(Collectors.toSet());

        List<Map<String, Object>> compRows = jdbc.queryForList(
                "SELECT dp.amount, p.id AS product_id, p.name AS product_name " +
                        "FROM dishes_products dp " +
                        "JOIN products p ON p.id = dp.product_id " +
                        "WHERE dp.dish_id = ?", id);

        List<Ingredient> composition = compRows.stream()
                .map(r -> Ingredient.builder()
                        .productId(extractUUID(r.get("product_id")))
                        .productName((String) r.get("product_name"))
                        .amount(((Number) r.get("amount")).floatValue())
                        .build())
                .collect(Collectors.toList());

        return DishDto.builder()
                .id(dishId)
                .name((String) row.get("name"))
                .photos(photos.isEmpty() ? new ArrayList<>() : photos)
                .calorieContent(getDouble(row, "calorie_content"))
                .proteins(getDouble(row, "proteins"))
                .fats(getDouble(row, "fats"))
                .carbohydrates(getDouble(row, "carbohydrates"))
                .composition(composition)
                .size(getDouble(row, "size"))
                .category(DishCategory.valueOf((String) row.get("category")))
                .flags(flags)
                .creationDate(toLocalDateTime(row.get("created_at")))
                .editDate(toLocalDateTime(row.get("updated_at")))
                .build();
    }
}