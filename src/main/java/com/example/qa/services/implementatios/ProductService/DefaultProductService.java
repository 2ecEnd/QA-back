package com.example.qa.services.implementatios.ProductService;

import com.example.qa.mappers.ProductMapper;
import com.example.qa.models.dto.ChangeEntityResponse;
import com.example.qa.models.dto.CreateEntityResponse;
import com.example.qa.models.dto.DeleteEntityResponse;
import com.example.qa.models.dto.products.ChangeProductRequest;
import com.example.qa.models.dto.products.CreateProductRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultProductService implements ProductService{

    private final ProductRepository productRepository;
    private final DishProductRepository dishProductRepository;

    private final ProductMapper productMapper;

    private static final String path = "/products";

    @Override
    public ResponseEntity<CreateEntityResponse> createEntity(CreateProductRequest request) {
        var entityBuilder = Product.builder()
                .name(request.getName())
                .photos(request.getPhotos())
                .calorieContent(request.getCalorieContent())
                .proteins(request.getProteins())
                .fats(request.getFats())
                .carbohydrates(request.getCarbohydrates())
                .category(request.getCategory())
                .cookingNecessity(request.getCookingNecessity())
                .flags(request.getFlags());

        if (request.getComposition() != null) {
            entityBuilder.composition(request.getComposition());
        }

        var entity = entityBuilder.build();
        productRepository.save(entity);

        return ResponseEntity.created(URI.create(path + "/" + entity.getId().toString())).body(
                new CreateEntityResponse(entity.getId())
        );
    }

    @Override
    public ResponseEntity<List<ProductDto>> getEntities(
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
                predicates.add(cb.equal(root.get("readinessDegree"), cookingNecessity));
            }
            if (flags != null) {
                predicates.add(cb.equal(root.get("flags"), flags));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        var products = sort == null ?
                productRepository.findAll(specs)
                                  .stream()
                                  .map(productMapper::toDto)
                                  .toList() :
                productRepository.findAll(
                        specs,
                        Sort.by(Sort.Direction.ASC, sort.toString()))
                                  .stream()
                                  .map(productMapper::toDto)
                                  .toList();

        return ResponseEntity.ok(products.stream()
                .filter(productDto -> productDto.getName().toLowerCase().contains(search.toLowerCase()))
                .toList());
    }

    @Override
    public ResponseEntity<ProductDto> getEntity(UUID id) {
        var entity = productRepository.findById(id);

        return entity.map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ChangeEntityResponse> changeEntity(UUID id, ChangeProductRequest request) {
        var entityOpt = productRepository.findById(id);
        if (entityOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var entity = entityOpt.get();

        entity.setName(request.name);
        entity.setPhotos(request.photos);
        entity.setCalorieContent(request.calorieContent);
        entity.setProteins(request.proteins);
        entity.setFats(request.fats);
        entity.setCarbohydrates(request.carbohydrates);
        entity.setComposition(request.composition);
        entity.setCategory(request.category);
        entity.setCookingNecessity(request.cookingNecessity);
        entity.setFlags(request.flags);

        productRepository.save(entity);

        return ResponseEntity.ok().body(new ChangeEntityResponse(1));
    }

    @Override
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (dishProductRepository.existsDishWithProduct(id)) {
            return ResponseEntity.status(409).build();
        }

        productRepository.deleteById(id);
        return ResponseEntity.ok().body(new DeleteEntityResponse(1));
    }
}