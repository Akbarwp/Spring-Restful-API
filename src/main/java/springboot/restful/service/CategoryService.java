package springboot.restful.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import springboot.restful.entity.Category;
import springboot.restful.entity.User;
import springboot.restful.model.CategoryResponse;
import springboot.restful.repository.CategoryRepository;
import springboot.restful.request.CreateCategoryRequest;
import springboot.restful.request.UpdateCategoryRequest;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ValdiationService valdiationService;

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }

    @Transactional
    public CategoryResponse create(User user, CreateCategoryRequest request) {
        valdiationService.validate(request);

        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(request.getName());
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        return toCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse get(User user, String id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse update(User user, UpdateCategoryRequest request) {
        valdiationService.validate(request);

        Category category = categoryRepository.findById(request.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(request.getName());
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        return toCategoryResponse(category);
    }

    @Transactional
    public void delete(User user, String id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
    }
}
