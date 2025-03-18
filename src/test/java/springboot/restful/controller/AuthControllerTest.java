package springboot.restful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.restful.entity.User;
import springboot.restful.model.TokenResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.LoginUserRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.sql.Timestamp;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testLoginFailed() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("Admin123");

        mockMvc.perform(
            post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpectAll(
                status().isUnauthorized())
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testLoginFailedWrongPassword() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("admin@gmail.com");
        user.setPassword(BCrypt.hashpw("Admin123", BCrypt.gensalt()));
        user.setName("Admin Admin");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("Test123");

        mockMvc.perform(
            post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpectAll(
                status().isUnauthorized())
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("admin@gmail.com");
        user.setPassword(BCrypt.hashpw("Admin123", BCrypt.gensalt()));
        user.setName("Admin Admin");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("Admin123");

        mockMvc.perform(
            post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpectAll(
                status().isOk())
            .andDo(result -> {
                WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                TokenResponse data = objectMapper.convertValue(response.getData(), TokenResponse.class);
                assertEquals(data, response.getData());

                assertNull(response.getErrors());
                assertNotNull(response.getData().getToken());
                assertNotNull(response.getData().getExpiredAt());

                User userDB = userRepository.findById(request.getEmail()).orElse(null);
                assertNotNull(userDB);
                assertEquals(userDB.getToken(), response.getData().getToken());
                assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
            });
    }
}
