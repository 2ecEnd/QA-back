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
import com.example.qa.models.enums.ReadinessDegree;
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

    final ProductRepository productRepository;
    final DishProductRepository dishProductRepository;

    final ProductMapper productMapper;

    static final String path = "/products";

    @Override
    public ResponseEntity<CreateEntityResponse> createEntity(CreateProductRequest request) {
        var entityBuilder = Product.builder()
                .name(request.name)
                .photos(request.photos)
                .calorieContent(request.calorieContent)
                .proteins(request.proteins)
                .fats(request.fats)
                .carbohydrates(request.carbohydrates)
                .category(request.category)
                .readinessDegree(request.readinessDegree);

        if (request.composition != null) {
            entityBuilder.composition(request.composition);
        }
        if (request.flags != null) {
            entityBuilder.flags(request.flags);
        }

        var entity = entityBuilder.build();
        productRepository.save(entity);

        return ResponseEntity.created(URI.create(path + "/" + entity.id.toString())).body(
                new CreateEntityResponse(entity.id)
        );
    }

    @Override
    public ResponseEntity<List<ProductDto>> getEntities(
            ProductCategory category,
            ReadinessDegree readinessDegree,
            List<Flag> flags,
            String search,
            SortField sort
    ) {
        Specification<Product> specs = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (readinessDegree != null) {
                predicates.add(cb.equal(root.get("readinessDegree"), readinessDegree));
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
                .filter(productDto -> productDto.name.toLowerCase().contains(search.toLowerCase()))
                .toList());
    }

    @Override
    public ResponseEntity<ProductDto> getEntity(UUID request) {
        var entity = productRepository.findById(request);

        return entity.map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ChangeEntityResponse> changeEntity(ChangeProductRequest request) {
        var entity = productRepository.findById(request.id);

        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        entity.get().name = request.name;
        entity.get().photos = request.photos;
        entity.get().calorieContent = request.calorieContent;
        entity.get().proteins = request.proteins;
        entity.get().fats = request.fats;
        entity.get().carbohydrates = request.carbohydrates;
        entity.get().composition = request.composition;
        entity.get().category = request.category;
        entity.get().readinessDegree = request.readinessDegree;
        entity.get().flags = request.flags;

        productRepository.save(entity.get());

        return ResponseEntity.ok().body(new ChangeEntityResponse(1));
    }

    @Override
    public ResponseEntity<DeleteEntityResponse> deleteEntity(UUID request) {
        if (!productRepository.existsById(request)) {
            return ResponseEntity.notFound().build();
        }

        if (dishProductRepository.existsDishWithProduct(request)) {
            return ResponseEntity.status(409).build();
        }

        productRepository.deleteById(request);
        return ResponseEntity.ok().body(new DeleteEntityResponse(1));
    }
}