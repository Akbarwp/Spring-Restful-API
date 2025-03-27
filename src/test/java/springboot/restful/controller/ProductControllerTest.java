package springboot.restful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.restful.entity.Category;
import springboot.restful.entity.Product;
import springboot.restful.entity.User;
import springboot.restful.model.*;
import springboot.restful.repository.AddressRepository;
import springboot.restful.repository.CategoryRepository;
import springboot.restful.repository.ContactRepository;
import springboot.restful.repository.ProductRepository;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.CreateProductRequest;
import springboot.restful.request.UpdateProductRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("admin@gmail.com");
        user.setPassword(BCrypt.hashpw("Admin123", BCrypt.gensalt()));
        user.setName("Admin Admin");
        user.setToken("TestToken");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 16 * 24 * 30));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Category category = new Category();
        category.setId("TestCategory");
        category.setName("Computer & Laptop");
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        categoryRepository.save(category);
    }

    @Test
    void testCreateProductBadRequest() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("");
        request.setPriceBuy(0.00);
        request.setPriceSell(0.00);
        request.setStock(0);
        request.setDescription("");
        request.setCategoryId(null);

        mockMvc.perform(
                post("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isBadRequest()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testCreateProductSuccess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        CreateProductRequest request = new CreateProductRequest();
        request.setName("Rexus Daxa Air IV");
        request.setPriceBuy(800000.00);
        request.setPriceSell(900000.00);
        request.setStock(100);
        request.setDescription("Gaming mouse high end");
        request.setCategoryId(category.getId());

        mockMvc.perform(
                post("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getName(), response.getData().getName());
                assertEquals(request.getPriceBuy(), response.getData().getPriceBuy());
                assertEquals(request.getPriceSell(), response.getData().getPriceSell());
                assertEquals(request.getStock(), response.getData().getStock());
                assertEquals(request.getDescription(), response.getData().getDescription());
                assertEquals(request.getCategoryId(), response.getData().getCategory().getId());
                assertTrue(productRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testGetProductNotFound() throws Exception {
        mockMvc.perform(
                get("/products/notfound")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isNotFound()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testGetProductSuccess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Rexus Daxa Air IV");
        product.setPriceBuy(800000.00);
        product.setPriceSell(900000.00);
        product.setStock(100);
        product.setDescription("Gaming mouse high end");
        product.setCategory(category);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        productRepository.save(product);

        mockMvc.perform(
                get("/products/" + product.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(product.getId(), response.getData().getId());
                assertEquals(product.getName(), response.getData().getName());
                assertEquals(product.getPriceBuy(), response.getData().getPriceBuy());
                assertEquals(product.getPriceSell(), response.getData().getPriceSell());
                assertEquals(product.getStock(), response.getData().getStock());
                assertEquals(product.getDescription(), response.getData().getDescription());
                assertEquals(product.getCategory().getId(), response.getData().getCategory().getId());
            });
    }

    @Test
    void testUpdateProductBadRequest() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("");
        request.setPriceBuy(0.00);
        request.setPriceSell(0.00);
        request.setStock(0);
        request.setDescription("");
        request.setCategoryId(null);

        mockMvc.perform(
                put("/products/notfound")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isBadRequest()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Rexus Daxa Air IV");
        product.setPriceBuy(800000.00);
        product.setPriceSell(900000.00);
        product.setStock(100);
        product.setDescription("Gaming mouse high end");
        product.setCategory(category);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        productRepository.save(product);

        Category categoryNew = new Category();
        categoryNew.setId(UUID.randomUUID().toString());
        categoryNew.setName("Gaming");
        categoryNew.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        categoryNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        categoryRepository.save(categoryNew);

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Rexus Daxa Air Mini");
        request.setPriceBuy(850000.00);
        request.setPriceSell(950000.00);
        request.setStock(50);
        request.setDescription("Gaming mouse high end");
        request.setCategoryId(categoryNew.getId());

        mockMvc.perform(
                put("/products/" + product.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getName(), response.getData().getName());
                assertEquals(request.getPriceBuy(), response.getData().getPriceBuy());
                assertEquals(request.getPriceSell(), response.getData().getPriceSell());
                assertEquals(request.getStock(), response.getData().getStock());
                assertEquals(request.getDescription(), response.getData().getDescription());
                assertEquals(request.getCategoryId(), response.getData().getCategory().getId());
                assertTrue(productRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        mockMvc.perform(
                delete("/products/notfound")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isNotFound()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testDeleteProductSuccess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Rexus Daxa Air IV");
        product.setPriceBuy(800000.00);
        product.setPriceSell(900000.00);
        product.setStock(100);
        product.setDescription("Gaming mouse high end");
        product.setCategory(category);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        productRepository.save(product);

        mockMvc.perform(
                delete("/products/" + product.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("Delete product success", response.getMessages());
            });
    }

    @Test
    void testSearchProductNotFound() throws Exception {
        mockMvc.perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(0, response.getData().size());
                assertEquals(0, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });
    }

    @Test
    void testSearchProductSucess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        for (int i = 1; i <= 21; i++) {
            Product product = new Product();
            product.setId(UUID.randomUUID().toString());
            product.setName("Product" + i);
            product.setPriceBuy((i*1000.00));
            product.setPriceSell((i*1000.00) + (i*1000.00*0.3));
            product.setStock(i*10);
            product.setCategory(category);
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            productRepository.save(product);
        }

        mockMvc.perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("name", "Product")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("priceBuy", "0.00")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("priceSell", "0.00")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("name", "product")
                    .queryParam("page", "1")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(1, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });
    }

    @Test
    void testListProductNotFound() throws Exception {
        mockMvc.perform(
                get("/categories/notfound/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isNotFound()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testListProductSuccess() throws Exception {
        Category category = categoryRepository.findById("TestCategory").orElseThrow();

        for (int i = 1; i <= 5; i++) {
            Product product = new Product();
            product.setId(UUID.randomUUID().toString());
            product.setName("Product" + i);
            product.setPriceBuy((i*1000.00));
            product.setPriceSell((i*1000.00) + (i*1000.00*0.3));
            product.setStock(i*10);
            product.setCategory(category);
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            productRepository.save(product);
        }

        mockMvc.perform(
                get("/categories/" + category.getId() + "/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(5, response.getData().size());
            });
    }

}
