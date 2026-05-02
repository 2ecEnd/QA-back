package com.example.qa.services.implementatios.DishService;

import com.example.qa.mappers.DishMapper;
import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
import com.example.qa.models.dto.dishes.ChangeDishRequest;
import com.example.qa.models.dto.dishes.CreateDishRequest;
import com.example.qa.models.dto.dishes.DishDto;
import com.example.qa.models.entities.Dish;
import com.example.qa.models.entities.DishProduct;
import com.example.qa.models.enums.DishCategory;
import com.example.qa.models.enums.Flag;
import com.example.qa.repositories.DishProductRepository;
import com.example.qa.repositories.DishRepository;
import com.example.qa.repositories.ProductRepository;
import com.example.qa.services.DishService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultDishService implements DishService {

    final DishRepository dishRepository;
    final ProductRepository productRepository;
    final DishProductRepository dishProductRepository;

    final DishMapper dishMapper;

    @Override
    public UUID createEntity(CreateDishRequest request) {
        // Проверка, что существуют все необходимые продукты
        for (var ingredient : request.composition) {
            if(productRepository.findById(ingredient.productId).isEmpty()) {
                return null;
            }
        }

        Dish entity = dishMapper.toEntity(request);
        dishRepository.save(entity);

        return entity.getId();
    }

    @Override
    public List<DishDto> getEntities(
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

        var dishes = dishRepository.findAll(specs).stream().map(dishMapper::toDto);

        return search == null ?
                dishes.toList() :
                dishes.filter(dishDto ->
                        dishDto.getName().toLowerCase().contains(search.toLowerCase())
                ).toList();
    }

    @Override
    public DishDto getEntity(UUID request) {
        Optional<Dish> dish = dishRepository.findById(request);

        return dish.map(dishMapper::toDto).orElse(null);
    }

    @Override
    public Integer changeEntity(UUID id, ChangeDishRequest request) {
        Optional<Dish> dishTmp = dishRepository.findById(id);
        if (dishTmp.isEmpty()) {
            return 0;
        }
        var dish = dishTmp.get();

        dish.setName(request.getName());
        dish.setPhotos(request.getPhotos());
        dish.setCalorieContent(request.getCalorieContent());
        dish.setProteins(request.getProteins());
        dish.setFats(request.getFats());
        dish.setCarbohydrates(request.getCarbohydrates());
        dish.setSize(request.getSize());
        dish.setCategory(request.getCategory());
        dish.setFlags(request.getFlags());

        List<DishProduct> currentComposition = dish.getComposition();
        currentComposition.clear();

        List<DishProduct> newComposition = request.getComposition().stream()
                .map(ingredient -> DishProduct.builder()
                        .product(productRepository.findById(ingredient.productId).get())
                        .dish(dish)
                        .amount(ingredient.amount)
                        .build()
                )
                .toList();
        currentComposition.addAll(newComposition);
        dishRepository.save(dish);

        return 1;
    }

    @Override
    public DeleteDishAcknowledge deleteEntity(UUID id) {
        if (!dishRepository.existsById(id)) {
            return new DeleteDishAcknowledge(false);
        }

        dishRepository.deleteById(id);
        return new DeleteDishAcknowledge(true);
    }
}