package com.example.qa.models.entities;

import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.CookingNecessity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

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
    @DecimalMax("100")
    private Double proteins;

    @Column(name = "fats", nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private Double fats;

    @Column(name = "carbohydrates", nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private Double carbohydrates;

    @Column(name = "composition", nullable = true)
    @Builder.Default
    private String composition = null;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "readiness_degree", nullable = false)
    @Enumerated(EnumType.STRING)
    private CookingNecessity cookingNecessity;

    @ElementCollection(targetClass = Flag.class)
    @Column(name = "flags", nullable = true)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Flag> flags = null;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JoinColumn(name = "dishProducts", nullable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishProduct> dishProducts;

    @PrePersist
    @PreUpdate
    private void validateMacroSum() {
        double sum = proteins + fats + carbohydrates;
        if (sum > 100.0) {
            throw new IllegalStateException("Сумма белков, жиров и углеводов на 100г не может превышать 100г");
        }
    }
}