package springboot.restful.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.restful.entity.Contact;
import springboot.restful.entity.User;
import springboot.restful.model.ContactResponse;
import springboot.restful.repository.ContactRepository;
import springboot.restful.request.CreateContactRequest;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValdiationService valdiationService;

    public ContactResponse response(Contact contact) {
        return ContactResponse.builder()
            .id(contact.getId())
            .firstname(contact.getFirstname())
            .lastname(contact.getLastname())
            .email(contact.getEmail())
            .phone(contact.getPhone())
            .createdAt(contact.getCreatedAt())
            .updatedAt(contact.getUpdatedAt())
            .build();
    }

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        valdiationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname(request.getFirstname());
        contact.setLastname(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        contactRepository.save(contact);

        return response(contact);
    }
}
