package com.example.qa.services.implementatios.DishService;

import com.example.qa.mappers.DishMapper;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.DishProduct;
import com.example.qa.models.entities.Product;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.repositories.DishProductRepository;
import com.example.qa.repositories.DishRepository;
import com.example.qa.repositories.ProductRepository;
import com.example.qa.services.DishService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultDishService implements DishService {

    final DishRepository dishRepository;
    final ProductRepository productRepository;
    final DishProductRepository dishProductRepository;

    final DishMapper dishMapper;

    static final String path = "/dishes";

    @Override
    public ResponseEntity<CreateEntityResponse> createEntity(CreateDishRequest request) {
        // Проверка, что существуют все необходимые продукты
        for (var ingridient : request.composition) {
            if(productRepository.findById(ingridient.productId).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
        }

        // Создание сущности
        var entityBuilder = Dish.builder()
                .name(request.name)
                .photos(request.photos)
                .calorieContent(request.calorieContent)
                .proteins(request.proteins)
                .fats(request.fats)
                .carbohydrates(request.carbohydrates)
                .composition(List.of())
                .size(request.size);

        if (request.flags != null) {
            entityBuilder.flags(request.flags);
        }

        var entity = entityBuilder.build();
        dishRepository.save(entity);

        // Добавление состава сущности
        request.composition.forEach(ingridient -> {
            var dp = DishProduct.builder()
                    .dish(entity)
                    .product(productRepository.findById(ingridient.productId).get())
                    .amount(ingridient.amount)
                    .build();

            dishProductRepository.save(dp);
            entity.composition.add(dp);
        });

        dishRepository.save(entity);

        return ResponseEntity.created(URI.create(path + "/" + entity.id.toString())).body(
                new CreateEntityResponse(entity.id)
        );
    }

    @Override
    public ResponseEntity<List<DishDto>> getEntities(
            DishCategory category,
            List<Flag> flags,
            String search
    ) {
        Specification<Dish> specs = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (flags != null) {
                predicates.add(cb.equal(root.get("flags"), flags));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return ResponseEntity.ok(dishRepository.findAll(specs)
                                  .stream()
                                  .map(dishMapper::toDto)
                                  .toList());
    }

    @Override
    public ResponseEntity<DishDto> getEntity(UUID request) {
        var entity = dishRepository.findById(request);

        return entity.map(product -> ResponseEntity.ok(dishMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeDishRequest request) {
        var entityTmp = dishRepository.findById(request.id);

        if (entityTmp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var entity = entityTmp.get();

        //TODO: добавить удаление ингредиентов
        entity.name = request.name;
        entity.photos = request.photos;
        entity.calorieContent = request.calorieContent;
        entity.proteins = request.proteins;
        entity.fats = request.fats;
        entity.carbohydrates = request.carbohydrates;
        entity.composition = request.composition.stream()
                .map(ingridient -> DishProduct.builder()
                        .product(productRepository.findById(ingridient.productId).get())
                        .dish(entity)
                        .amount(ingridient.amount)
                        .build()
                )
                .toList();
        entity.size = request.size;
        entity.category = request.category;
        entity.flags = request.flags;

        dishRepository.save(entity);

        return ResponseEntity.ok().body(new ChangeEntityResponse(1));
    }

    @Override
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request) {
        if (!dishRepository.existsById(request)) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(request);
        return ResponseEntity.ok().body(new DeleteEntityResponse(1));
    }
}