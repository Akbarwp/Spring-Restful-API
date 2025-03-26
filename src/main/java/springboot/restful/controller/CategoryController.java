package springboot.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.CategoryResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.CreateCategoryRequest;
import springboot.restful.request.UpdateCategoryRequest;
import springboot.restful.service.CategoryService;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(
        path = "/categories",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> create(User user, @RequestBody CreateCategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.create(user, request);

        return WebResponse.<CategoryResponse> builder()
            .messages("Create category success")
            .data(categoryResponse)
            .build();
    }

    @GetMapping(
        path = "/categories/{categoryId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> get(User user, @PathVariable(value = "categoryId") String categoryId) {
        CategoryResponse categoryResponse = categoryService.get(user, categoryId);

        return WebResponse.<CategoryResponse> builder()
            .messages("Get category success")
            .data(categoryResponse)
            .build();
    }

    @PutMapping(
        path = "/categories/{categoryId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> update(User user, @RequestBody UpdateCategoryRequest request, @PathVariable(value = "categoryId") String categoryId) {
        request.setId(categoryId);
        CategoryResponse categoryResponse = categoryService.update(user, request);

        return WebResponse.<CategoryResponse> builder()
            .messages("Update category success")
            .data(categoryResponse)
            .build();
    }

    @DeleteMapping(
        path = "/categories/{categoryId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable(value = "categoryId") String categoryId) {
        categoryService.delete(user, categoryId);

        return WebResponse.<String> builder()
            .messages("Delete category success")
            .build();
    }
}
