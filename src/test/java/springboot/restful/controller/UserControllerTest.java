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

import springboot.restful.entity.User;
import springboot.restful.model.*;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.RegisterUserRequest;
import springboot.restful.request.UpdateUserRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

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
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("Admin123");
        request.setName("Admin Admin");

        mockMvc.perform(
                post("/auth/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());

                UserResponse data = objectMapper.convertValue(response.getData(), UserResponse.class);
                assertEquals(data, response.getData());
            });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/auth/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
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
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("ucup@gmail.com");
        user.setPassword(BCrypt.hashpw("Ucup123", BCrypt.gensalt()));
        user.setName("Ucup bin Otong");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("ucup@gmail.com");
        request.setPassword("Ucup123");
        request.setName("Ucup bin Otong");

        mockMvc.perform(
                post("/auth/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
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
    void testUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "notfound")
            )
            .andExpectAll(
                status().isUnauthorized()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testUserUnauthorizedTokenNotSend() throws Exception {
        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isUnauthorized()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testGetUserSuccess() throws Exception {
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

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", user.getToken())
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("admin@gmail.com", response.getData().getEmail());
                assertEquals("Admin Admin", response.getData().getName());
            });
    }

    @Test
    void testGetUserTokenExpired() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("admin@gmail.com");
        user.setPassword(BCrypt.hashpw("Admin123", BCrypt.gensalt()));
        user.setName("Admin Admin");
        user.setToken("TestToken");
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 16 * 24 * 30));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", user.getToken())
            )
            .andExpectAll(
                status().isUnauthorized()
            )
            .andDo(result -> {
                WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testUpdateUserUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpectAll(
                status().isUnauthorized()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(response.getErrors());
            });
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
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

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Ucup bin Otong");
        request.setPassword("AdminGanteng123;");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                User userDB = userRepository.findById("admin@gmail.com").orElse(null);

                assertNull(response.getErrors());
                assertEquals("Ucup bin Otong", response.getData().getName());
                assertEquals("admin@gmail.com", response.getData().getEmail());
                assertTrue(BCrypt.checkpw("AdminGanteng123;", userDB.getPassword()));
            });
    }
}
