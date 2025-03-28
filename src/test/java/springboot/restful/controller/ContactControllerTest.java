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

import springboot.restful.entity.Contact;
import springboot.restful.entity.User;
import springboot.restful.model.*;
import springboot.restful.repository.AddressRepository;
import springboot.restful.repository.ContactRepository;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.CreateContactRequest;
import springboot.restful.request.UpdateContactRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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
    void testCreateContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("");
        request.setLastname("");
        request.setEmail("");
        request.setPhone("");

        mockMvc.perform(
                post("/contacts")
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
    void testCreateContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("Ucup");
        request.setLastname("bin Otong");
        request.setEmail("ucup@gmail.com");
        request.setPhone("081234567890");

        mockMvc.perform(
                post("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("Ucup", response.getData().getFirstname());
                assertEquals("bin Otong", response.getData().getLastname());
                assertEquals("ucup@gmail.com", response.getData().getEmail());
                assertEquals("081234567890", response.getData().getPhone());
                assertTrue(contactRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testGetContactNotFound() throws Exception {
        mockMvc.perform(
                get("/contacts/123456")
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
    void testGetContactSuccess() throws Exception {
        User user = userRepository.findById("admin@gmail.com").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname("Ucup");
        contact.setLastname("bin Otong");
        contact.setEmail("ucup@gmail.com");
        contact.setPhone("081234567890");
        contact.setUser(user);
        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        contactRepository.save(contact);

        mockMvc.perform(
                get("/contacts/" + contact.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(contact.getId(), response.getData().getId());
                assertEquals(contact.getFirstname(), response.getData().getFirstname());
                assertEquals(contact.getLastname(), response.getData().getLastname());
                assertEquals(contact.getEmail(), response.getData().getEmail());
                assertEquals(contact.getPhone(), response.getData().getPhone());
            });
    }

    @Test
    void testUpdateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstname("");
        request.setLastname("");
        request.setEmail("");
        request.setPhone("");

        mockMvc.perform(
                put("/contacts/123456")
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
    void testUpdateContactSuccess() throws Exception {
        User user = userRepository.findById("admin@gmail.com").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname("Ucup");
        contact.setLastname("bin Otong");
        contact.setEmail("admin@gmail.com");
        contact.setPhone("081234567890");
        contact.setUser(user);
        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstname("Otong");
        request.setLastname("Surotong");
        request.setEmail("ucup@gmail.com");
        request.setPhone("081234567891");

        mockMvc.perform(
                put("/contacts/" + contact.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getFirstname(), response.getData().getFirstname());
                assertEquals(request.getLastname(), response.getData().getLastname());
                assertEquals(request.getEmail(), response.getData().getEmail());
                assertEquals(request.getPhone(), response.getData().getPhone());
                assertTrue(contactRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testDeleteContactNotFound() throws Exception {
        mockMvc.perform(
                delete("/contacts/123456")
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
    void testDeleteContactSuccess() throws Exception {
        User user = userRepository.findById("admin@gmail.com").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname("Ucup");
        contact.setLastname("bin Otong");
        contact.setEmail("admin@gmail.com");
        contact.setPhone("081234567890");
        contact.setUser(user);
        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        contactRepository.save(contact);

        mockMvc.perform(
                delete("/contacts/" + contact.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("Delete contact success", response.getMessages());
            });
    }

    @Test
    void testSearchContactNotFound() throws Exception {
        mockMvc.perform(
                get("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(0, response.getData().size());
                assertEquals(0, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });
    }

    @Test
    void testSearchContactSucess() throws Exception {
        User user = userRepository.findById("admin@gmail.com").orElseThrow();

        for (int i = 1; i <= 21; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstname("Contact " + i);
            contact.setLastname("bin Otong");
            contact.setEmail("contact@gmail.com");
            contact.setPhone("08123456789" + i);
            contact.setUser(user);
            contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("name", "Contact")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("email", "contact@gmail.com")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("phone", "08123456789")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(0, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });

        mockMvc.perform(
                get("/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
                    .queryParam("name", "Contact")
                    .queryParam("page", "1")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(10, response.getData().size());
                assertEquals(3, response.getPaging().getTotalPage());
                assertEquals(1, response.getPaging().getCurrentPage());
                assertEquals(10, response.getPaging().getSize());
            });
    }
}
