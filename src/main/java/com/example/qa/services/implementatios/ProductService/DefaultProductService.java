package com.example.qa.services.implementatios.ProductService;

import com.example.qa.mappers.DishMapper;
import com.example.qa.mappers.ProductMapper;
import com.example.qa.models.dto.dishes.DishShortInfoDto;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
import com.example.qa.models.dto.products.DeleteProductAcknowledge;
import com.example.qa.models.dto.products.ProductDto;
import com.example.qa.models.entities.Product;
import com.example.qa.models.enums.Flag;
import com.example.qa.models.enums.ProductCategory;
import com.example.qa.models.enums.CookingNecessity;
import com.example.qa.models.enums.SortField;
import com.example.qa.repositories.DishProductRepository;
import com.example.qa.repositories.ProductRepository;
import com.example.qa.services.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultProductService implements ProductService{

    private final ProductRepository productRepository;
    private final DishProductRepository dishProductRepository;

    private final ProductMapper productMapper;
    private final DishMapper dishMapper;

    private static final String path = "/products";

    @Override
    public UUID createEntity(CreateProductRequest request) {
        var entity = productMapper.createRequestToEntity(request);
        productRepository.save(entity);

        return entity.getId();
    }

    @Override
    public List<ProductDto> getEntities(
            ProductCategory category,
            CookingNecessity cookingNecessity,
            List<Flag> flags,
            String search,
            SortField sort
    ) {
        Specification<Product> specs = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (cookingNecessity != null) {
                predicates.add(cb.equal(root.get("cookingNecessity"), cookingNecessity));
            }
            if (flags != null) {
                List<Predicate> memberPredicates = flags.stream()
                        .map(flag -> cb.isMember(flag, root.get("flags")))
                        .toList();
                predicates.addAll(memberPredicates);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<ProductDto> products = sort == null ?
                productRepository.findAll(specs)
                                    .stream()
                                    .map(productMapper::entityToDto)
                                    .toList() :
                productRepository.findAll(specs, Sort.by(Sort.Direction.ASC, sort.getFieldName()))
                                    .stream()
                                    .map(productMapper::entityToDto)
                                    .toList();

        return search == null ?
                products :
                products.stream()
                    .filter(productDto -> productDto.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
    }

    @Override
    public ProductDto getEntity(UUID id) {
        Optional<Product> entity = productRepository.findById(id);

        return entity.map(productMapper::entityToDto).orElse(null);
    }

    @Override
    public Integer changeEntity(UUID id, ChangeProductRequest request) {
        Optional<Product> productTmp = productRepository.findById(id);
        if (productTmp.isEmpty()) {
            return 0;
        }
        Product product = productTmp.get();

        product.setName(request.name);
        product.setPhotos(request.photos);
        product.setCalorieContent(request.calorieContent);
        product.setProteins(request.proteins);
        product.setFats(request.fats);
        product.setCarbohydrates(request.carbohydrates);
        product.setComposition(request.composition);
        product.setCategory(request.category);
        product.setCookingNecessity(request.cookingNecessity);
        product.setFlags(request.flags);

        productRepository.save(product);

        return 1;
    }

    @Override
    public DeleteProductAcknowledge deleteEntity(UUID id) {
        if (!productRepository.existsById(id)) {
            return DeleteProductAcknowledge.builder()
                    .acknowledge(false)
                    .build();
        }

        List<DishShortInfoDto> dishesWithProduct = dishProductRepository.getDishesWithProduct(id).stream()
                .map(dishMapper::toShortInfo)
                .toList();
        if (!dishesWithProduct.isEmpty()) {
            return DeleteProductAcknowledge.builder()
                    .acknowledge(false)
                    .dishes(dishesWithProduct)
                    .build();
        }

        productRepository.deleteById(id);
        return DeleteProductAcknowledge.builder()
                .acknowledge(true)
                .build();
    }
}