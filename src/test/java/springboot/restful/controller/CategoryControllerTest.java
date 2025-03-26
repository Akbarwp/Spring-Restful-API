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
import springboot.restful.entity.User;
import springboot.restful.model.*;
import springboot.restful.repository.CategoryRepository;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.CreateCategoryRequest;
import springboot.restful.request.UpdateCategoryRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void testCreateCategoryBadRequest() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("");

        mockMvc.perform(
                post("/categories")
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
    void testCreateCategorySuccess() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Computer & Laptop");

        mockMvc.perform(
                post("/categories")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getName(), response.getData().getName());
                assertTrue(categoryRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testGetCategoryNotFound() throws Exception {
        mockMvc.perform(
                get("/categories/123456")
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
    void testGetCategorySuccess() throws Exception {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName("Computer & Laptop");
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        mockMvc.perform(
                get("/categories/" + category.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(category.getId(), response.getData().getId());
                assertEquals(category.getName(), response.getData().getName());
            });
    }

    @Test
    void testUpdateCategoryBadRequest() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("");

        mockMvc.perform(
                put("/categories/notfound")
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
    void testUpdateCategorySuccess() throws Exception {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName("Computer & Laptop");
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Smartphone");

        mockMvc.perform(
                put("/categories/" + category.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getName(), response.getData().getName());
                assertTrue(categoryRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testDeleteCategoryNotFound() throws Exception {
        mockMvc.perform(
                delete("/categories/notfound")
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
    void testDeleteCategorySuccess() throws Exception {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName("Computer & Laptop");
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        mockMvc.perform(
                delete("/categories/" + category.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("Delete category success", response.getMessages());
            });
    }
}
