package com.example.qa;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public abstract class BaseApiTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private JdbcTemplate jdbc;

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
}