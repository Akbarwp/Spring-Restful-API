package springboot.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.PagingResponse;
import springboot.restful.model.ProductResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.CreateProductRequest;
import springboot.restful.request.SearchProductRequest;
import springboot.restful.request.UpdateProductRequest;
import springboot.restful.service.ProductService;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(
        path = "/products",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> create(User user, @RequestBody CreateProductRequest request) {
        ProductResponse productResponse = productService.create(user, request);

        return WebResponse.<ProductResponse> builder()
            .messages("Create product success")
            .data(productResponse)
            .build();
    }

    @GetMapping(
        path = "/products/{productId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> get(
        User user,
        @PathVariable(value = "productId") String productId
    ) {

        ProductResponse productResponse = productService.get(user, productId);

        return WebResponse.<ProductResponse> builder()
            .messages("Get product success")
            .data(productResponse)
            .build();
    }

    @GetMapping(
        path = "/categories/{categoryId}/products",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductResponse>> listByCategory(
        User user,
        @PathVariable(value = "categoryId") String categoryId
    ) {

        List<ProductResponse> productResponses = productService.listByCategory(user, categoryId);

        return WebResponse.<List<ProductResponse>> builder()
            .messages("Get product success")
            .data(productResponses)
            .build();
    }

    @PutMapping(
        path = "/products/{productId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(
        User user,
        @RequestBody UpdateProductRequest request,
        @PathVariable(value = "productId") String productId
    ) {

        request.setProductId(productId);
        ProductResponse productResponse = productService.update(user, request);

        return WebResponse.<ProductResponse> builder()
            .messages("Update product success")
            .data(productResponse)
            .build();
    }

    @DeleteMapping(
        path = "/products/{productId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
        User user,
        @PathVariable(value = "productId") String productId
    ) {

        productService.delete(user, productId);

        return WebResponse.<String> builder()
            .messages("Delete product success")
            .build();
    }

    @GetMapping(
        path = "/products",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductResponse>> search(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priceBuy", required = false) Double priceBuy,
            @RequestParam(value = "priceSell", required = false) Double priceSell,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
        ) {

        SearchProductRequest request = SearchProductRequest.builder()
            .name(name)
            .priceBuy(priceBuy)
            .priceSell(priceSell)
            .stock(stock)
            .page(page)
            .size(size)
            .build();

        Page<ProductResponse> productResponse = productService.search(user, request);

        return WebResponse.<List<ProductResponse>> builder()
            .messages("Search product success")
            .data(productResponse.getContent())
            .paging(
                PagingResponse
                .builder()
                .currentPage(productResponse.getNumber())
                .totalPage(productResponse.getTotalPages())
                .size(productResponse.getSize())
                .build()
            )
            .build();
    }
}
