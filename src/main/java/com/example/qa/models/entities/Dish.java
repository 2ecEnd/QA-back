package com.example.qa.models.entities;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dishes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "name", nullable = false)
    public String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "photos", columnDefinition = "jsonb", nullable = false)
    @Size(min = 0, max = 5)
    public List<String> photos;

    @Column(name = "calorie_content", nullable = false)
    @Size(min = 0)
    public Float calorieContent;

    @Column(name = "proteins", nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    public Float proteins;

    @Column(name = "fats", nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    public Float fats;

    @Column(name = "carbohydrates", nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    public Float carbohydrates;

    @JoinColumn(name = "composition", nullable = false)
    @Size(min = 1)
    @OneToMany(cascade = CascadeType.ALL)
    public List<DishProduct> composition;

    @Column(name = "size", nullable = false)
    public Float size;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    public DishCategory category;

    @Column(name = "flags", nullable = true)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    public Set<Flag> flags = Collections.<Flag>emptySet();

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    public LocalDateTime creationDate;

    @Column(name = "edit_date", nullable = true)
    @LastModifiedDate
    public LocalDateTime editDate;
}