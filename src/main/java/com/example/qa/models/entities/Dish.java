package com.example.qa.models.entities;

import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;
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
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dishes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    @Size(min = 2)
    private String name;

    @ElementCollection
    @Column(name = "photos", nullable = true)
    @Size(min = 0, max = 5)
    @Builder.Default
    private List<String> photos = null;

    @Column(name = "calorie_content", nullable = false)
    @DecimalMin("0")
    private Double calorieContent;

    @Column(name = "proteins", nullable = false)
    @DecimalMin("0")
    private Double proteins;

    @Column(name = "fats", nullable = false)
    @DecimalMin("0")
    private Double fats;

    @Column(name = "carbohydrates", nullable = false)
    @DecimalMin("0")
    private Double carbohydrates;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Size(min = 1)
    private List<DishProduct> composition;

    @Column(name = "size", nullable = false)
    private Double size;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @ElementCollection(targetClass = Flag.class)
    @Column(name = "flags", nullable = true)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Flag> flags = null;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @PreUpdate
    private void onPreUpdate() {
        updatedAt = LocalDateTime.now();
    }
}