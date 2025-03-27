package springboot.restful.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;
import springboot.restful.entity.Category;
import springboot.restful.entity.Product;
import springboot.restful.entity.User;
import springboot.restful.model.CategoryResponse;
import springboot.restful.model.ProductResponse;
import springboot.restful.repository.CategoryRepository;
import springboot.restful.repository.ProductRepository;
import springboot.restful.request.CreateProductRequest;
import springboot.restful.request.SearchProductRequest;
import springboot.restful.request.UpdateProductRequest;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ValdiationService valdiationService;

    private ProductResponse toProductResponse(Product product) {
        Category category = categoryRepository.findById(product.getCategory().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        CategoryResponse categoryResponse = toCategoryResponse(category);

        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .priceBuy(product.getPriceBuy())
            .priceSell(product.getPriceSell())
            .stock(product.getStock())
            .description(product.getDescription())
            .category(categoryResponse)
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }

    @Transactional
    public ProductResponse create(User user, CreateProductRequest request) {
        valdiationService.validate(request);

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(request.getName());
        product.setPriceBuy(request.getPriceBuy());
        product.setPriceSell(request.getPriceSell());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse get(User user, String productid) {
        Product product = productRepository.findById(productid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listByCategory(User user, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        
        List<Product> products = productRepository.findAllByCategory(category);
        return products.stream().map(this::toProductResponse).toList();
    }

    @Transactional
    public ProductResponse update(User user, UpdateProductRequest request) {
        valdiationService.validate(request);

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setName(request.getName());
        product.setPriceBuy(request.getPriceBuy());
        product.setPriceSell(request.getPriceSell());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public void delete(User user, String productid) {
        Product product = productRepository.findById(productid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(User user, SearchProductRequest request) {
        Specification<Product> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            if (Objects.nonNull(request.getPriceBuy())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("priceBuy"), request.getPriceBuy()));
            }

            if (Objects.nonNull(request.getPriceSell())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("priceSell"), request.getPriceSell()));
            }

            if (Objects.nonNull(request.getStock())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("stock"), request.getStock()));
            }

            return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = products.getContent().stream().map(this::toProductResponse).toList();

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }
}
