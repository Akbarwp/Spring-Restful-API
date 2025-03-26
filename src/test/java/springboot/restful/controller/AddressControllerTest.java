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

import springboot.restful.entity.Address;
import springboot.restful.entity.Contact;
import springboot.restful.entity.User;
import springboot.restful.model.*;
import springboot.restful.repository.AddressRepository;
import springboot.restful.repository.ContactRepository;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.CreateAddressRequest;
import springboot.restful.request.UpdateAddressRequest;
import springboot.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

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

        Contact contact = new Contact();
        contact.setId("123456");
        contact.setFirstname("Ucup");
        contact.setLastname("bin Otong");
        contact.setEmail("ucup@gmail.com");
        contact.setPhone("081234567890");
        contact.setUser(user);
        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        contactRepository.save(contact);
    }

    @Test
    void testCreateAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("");
        request.setCity("");
        request.setProvince("");
        request.setCountry("");
        request.setPostalCode("");

        mockMvc.perform(
                post("/contacts/123456/addresses")
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
    void testCreateAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jalan Jenderal Basuki Rachmat");
        request.setCity("Surabaya");
        request.setProvince("Jawa Timur");
        request.setCountry("Indonesia");
        request.setPostalCode("60261");

        mockMvc.perform(
                post("/contacts/123456/addresses")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getStreet(), response.getData().getStreet());
                assertEquals(request.getCity(), response.getData().getCity());
                assertEquals(request.getProvince(), response.getData().getProvince());
                assertEquals(request.getCountry(), response.getData().getCountry());
                assertEquals(request.getPostalCode(), response.getData().getPostalCode());
                assertTrue(addressRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testGetAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/contacts/123456/addresses/notfound")
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
    void testGetAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet("Jalan Jenderal Basuki Rachmat");
        address.setCity("Surabaya");
        address.setProvince("Jawa Timur");
        address.setCountry("Indonesia");
        address.setPostalCode("60261");
        address.setContact(contact);
        address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        mockMvc.perform(
                get("/contacts/" + contact.getId() + "/addresses/" + address.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(address.getId(), response.getData().getId());
                assertEquals(address.getStreet(), response.getData().getStreet());
                assertEquals(address.getCity(), response.getData().getCity());
                assertEquals(address.getProvince(), response.getData().getProvince());
                assertEquals(address.getCountry(), response.getData().getCountry());
                assertEquals(address.getPostalCode(), response.getData().getPostalCode());
            });
    }

    @Test
    void testUpdateAddressBadRequest() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet("Jalan Jenderal Basuki Rachmat");
        address.setCity("Surabaya");
        address.setProvince("Jawa Timur");
        address.setCountry("Indonesia");
        address.setPostalCode("60261");
        address.setContact(contact);
        address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("");
        request.setCity("");
        request.setProvince("");
        request.setCountry("");
        request.setPostalCode("");

        mockMvc.perform(
                put("/contacts/" + contact.getId() + "/addresses/" + address.getId())
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
    void testUpdateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet("Jalan Jenderal Basuki Rachmat");
        address.setCity("Surabaya");
        address.setProvince("Jawa Timur");
        address.setCountry("Indonesia");
        address.setPostalCode("60261");
        address.setContact(contact);
        address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jalan Embong Malang");
        request.setCity("Surabaya");
        request.setProvince("Jawa Timur");
        request.setCountry("Indonesia");
        request.setPostalCode("60262");

        mockMvc.perform(
                put("/contacts/" + contact.getId() + "/addresses/" + address.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals(request.getStreet(), response.getData().getStreet());
                assertEquals(request.getCity(), response.getData().getCity());
                assertEquals(request.getProvince(), response.getData().getProvince());
                assertEquals(request.getCountry(), response.getData().getCountry());
                assertEquals(request.getPostalCode(), response.getData().getPostalCode());
                assertTrue(addressRepository.existsById(response.getData().getId()));
            });
    }

    @Test
    void testDeleteAddressNotFound() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();
        mockMvc.perform(
                delete("/contacts/" + contact.getId() + "/addresses/654321")
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
    void testDeleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();

        Address address = new Address();
        address.setId("TestAddress");
        address.setStreet("Jalan Jenderal Basuki Rachmat");
        address.setCity("Surabaya");
        address.setProvince("Jawa Timur");
        address.setCountry("Indonesia");
        address.setPostalCode("60261");
        address.setContact(contact);
        address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        mockMvc.perform(
                delete("/contacts/" + contact.getId() + "/addresses/" + address.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-API-TOKEN", "TestToken")
            )
            .andExpectAll(
                status().isOk()
            )
            .andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNull(response.getErrors());
                assertEquals("Delete address success", response.getMessages());
                assertFalse(addressRepository.existsById("TestAddress"));
            });
    }

    @Test
    void testListAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/contacts/notfound/addresses")
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
    void testListAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("123456").orElseThrow();

        for (int i = 1; i <= 5; i++) {
            Address address = new Address();
            address.setId(UUID.randomUUID().toString());
            address.setStreet("Jalan " + i);
            address.setCity("Surabaya");
            address.setProvince("Jawa Timur");
            address.setCountry("Indonesia");
            address.setPostalCode("60261");
            address.setContact(contact);
            address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/contacts/" + contact.getId() + "/addresses")
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
