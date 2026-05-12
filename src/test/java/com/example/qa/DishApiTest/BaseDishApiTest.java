package com.example.qa.DishApiTest;

import com.example.qa.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BaseDishApiTest extends BaseApiTest {

    protected UUID productA;
    protected UUID productB;

    @BeforeEach
    protected void addProducts() {
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
}