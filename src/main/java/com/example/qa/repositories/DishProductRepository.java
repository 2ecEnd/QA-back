package com.example.qa.repositories;

import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.DishProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DishProductRepository extends JpaRepository<DishProduct, UUID> {

    @Query(nativeQuery = true, value =
            "SELECT 1 " +
            "FROM dishes_products dp " +
            "WHERE dp.product_id = ?1")
    Integer existsDishWithProductById(UUID id);

    default Boolean existsDishWithProduct(UUID id) {
        return existsDishWithProductById(id) != null;
    }

    @Query(nativeQuery = true, value =
            "SELECT d.* " +
            "FROM dishes d " +
            "JOIN dishes_products dp ON d.id = dp.dish_id " +
            "WHERE dp.product_id = ?1")
    List<Dish> getDishesWithProduct(UUID productId);

    void deleteAllByDishId(UUID dishId);
}