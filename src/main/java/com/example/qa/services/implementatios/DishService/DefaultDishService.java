package com.example.qa.services.implementatios.DishService;

import com.example.qa.mappers.DishMapper;
import com.example.qa.models.dto.dishes.DeleteDishAcknowledge;
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
import com.example.qa.services.FileStorageService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultDishService implements DishService {

    private final DishRepository dishRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    private final DishMapper dishMapper;

    private static final Map<String, DishCategory> MACRO_CATEGORY_MAP = Map.of(
            "!десерт", DishCategory.DESSERT,
            "!первое", DishCategory.FIRST,
            "!второе", DishCategory.SECOND,
            "!напиток", DishCategory.DRINK,
            "!салат", DishCategory.SALAD,
            "!суп", DishCategory.SOUP,
            "!перекус", DishCategory.SNACK
    );
    private static final Pattern MACRO_PATTERN = Pattern.compile(
            "!(десерт|первое|второе|напиток|салат|суп|перекус)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    @Override
    public UUID createEntity(CreateDishRequest request) {
        // Проверка, что существуют все необходимые продукты
        for (var ingredient : request.composition) {
            if (productRepository.findById(ingredient.productId).isEmpty()) {
                return null;
            }
        }

        String name = request.name;
        DishCategory category = request.category;

        Matcher matcher = MACRO_PATTERN.matcher(name);
        if (matcher.find()) {
            String macro = matcher.group().toLowerCase();
            DishCategory macroCategory = MACRO_CATEGORY_MAP.get(macro);
            if (category == null && macroCategory != null) {
                category = macroCategory;
            }
            name = matcher.replaceAll("").trim().replaceAll("\\s+", " ");
        }
        request.name = name;
        request.category = category;

        Dish entity = dishMapper.toEntity(request);

        enforceFlags(entity, request.flags);

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
                List<Predicate> memberPredicates = flags.stream()
                        .map(flag -> cb.isMember(flag, root.get("flags")))
                        .toList();
                predicates.addAll(memberPredicates);
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
        Dish dish = dishTmp.get();
        List<String> oldPhotos = dish.getPhotos();

        String name = request.getName();
        DishCategory category = request.getCategory();

        if (name != null) {
            Matcher matcher = MACRO_PATTERN.matcher(name);
            if (matcher.find()) {
                String macro = matcher.group().toLowerCase();
                DishCategory macroCategory = MACRO_CATEGORY_MAP.get(macro);
                if (category == null && macroCategory != null) {
                    category = macroCategory;
                }
                name = matcher.replaceAll("").trim().replaceAll("\\s+", " ");
            }
            request.setName(name);
        }
        request.setCategory(category);

        // Применение основных полей
        dish.setName(request.getName());
        dish.setPhotos(request.getPhotos());
        dish.setCalorieContent(request.getCalorieContent());
        dish.setProteins(request.getProteins());
        dish.setFats(request.getFats());
        dish.setCarbohydrates(request.getCarbohydrates());
        dish.setSize(request.getSize());
        dish.setCategory(request.getCategory());

        // Обновление состава
        List<DishProduct> currentComposition = dish.getComposition();
        currentComposition.clear();
        List<DishProduct> newComposition = request.getComposition().stream()
                .map(ingredient -> DishProduct.builder()
                        .product(productRepository.findById(ingredient.productId).get())
                        .dish(dish)
                        .amount(ingredient.amount)
                        .build()
                )
                .collect(Collectors.toCollection(ArrayList::new));
        currentComposition.addAll(newComposition);

        Set<Flag> flagsToSet = request.getFlags() != null ? new HashSet<>(request.getFlags()) : null;
        enforceFlags(dish, flagsToSet);

        // Удаление старых фото
        if (oldPhotos != null && request.getPhotos() != null) {
            oldPhotos.stream()
                    .filter(url -> !request.getPhotos().contains(url))
                    .forEach(fileStorageService::deleteFile);
        } else if (oldPhotos != null) {
            oldPhotos.forEach(fileStorageService::deleteFile);
        }

        dishRepository.save(dish);
        return 1;
    }

    @Override
    public DeleteDishAcknowledge deleteEntity(UUID id) {
        Optional<Dish> dishOpt = dishRepository.findById(id);
        if (dishOpt.isEmpty()) {
            return new DeleteDishAcknowledge(false);
        }

        dishOpt.get().getPhotos().forEach(fileStorageService::deleteFile);
        dishRepository.deleteById(id);
        return new DeleteDishAcknowledge(true);
    }

    private void enforceFlags(Dish dish, Set<Flag> requestedFlags) {
        if (requestedFlags == null || requestedFlags.isEmpty()) {
            dish.setFlags(new HashSet<>());
            return;
        }

        List<Product> products = dish.getComposition().stream()
                .map(DishProduct::getProduct)
                .toList();

        if (products.isEmpty()) {
            dish.setFlags(new HashSet<>());
            return;
        }

        Set<Flag> allowedFlags = requestedFlags.stream()
                .filter(flag -> products.stream().allMatch(p -> p.getFlags().contains(flag)))
                .collect(Collectors.toCollection(HashSet::new));

        dish.setFlags(allowedFlags);
    }
}