package com.example.qa.models.entities;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "dishes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "name", nullable = false)
    public String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "photos", nullable = false)
    @Size(min = 0, max = 5)
    public List<String> photos;

    @Column(name = "calorie_content", nullable = false)
    @Size(min = 0)
    public Double calorieContent;

    @Column(name = "proteins", nullable = false)
    @Size(min = 0, max = 100)
    public Double proteins;

    @Column(name = "fats", nullable = false)
    @Size(min = 0, max = 100)
    public Double fats;

    @Column(name = "carbohydrates", nullable = false)
    @Size(min = 0, max = 100)
    public Double carbohydrates;

    @Column(name = "composition", nullable = false)
    @Size(min = 1)
    @ManyToMany
    public List<Product> composition;

    @Column(name = "size", nullable = false)
    public Double size;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    public DishCategory category;

    @Column(name = "flags", nullable = true)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    public Set<Flag> flags = Collections.<Flag>emptySet();

    @Column(name = "creation_date", nullable = true)
    @CreatedDate
    public LocalDateTime creationDate;

    @Column(name = "edit_date", nullable = true)
    @LastModifiedDate
    public LocalDateTime editDate;
}