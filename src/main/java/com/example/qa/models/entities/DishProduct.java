package com.example.qa.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.util.UUID;

@Entity
@Table(name = "dishes_products")
public class DishProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "dish_id", nullable = false)
    @OneToMany
    public UUID dishId;

    @Column(name = "product_id", nullable = false)
    @OneToMany
    public UUID productId;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    public Float amount;
}