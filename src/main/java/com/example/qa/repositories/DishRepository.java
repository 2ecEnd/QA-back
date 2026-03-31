package com.example.qa.repositories;

import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DishRepository
        extends JpaRepository<Dish, UUID>,
        JpaSpecificationExecutor<Dish> {
}