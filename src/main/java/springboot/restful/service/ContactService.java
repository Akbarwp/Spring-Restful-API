package springboot.restful.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;
import springboot.restful.entity.Contact;
import springboot.restful.entity.User;
import springboot.restful.model.ContactResponse;
import springboot.restful.repository.ContactRepository;
import springboot.restful.request.CreateContactRequest;
import springboot.restful.request.SearchContactRequest;
import springboot.restful.request.UpdateContactRequest;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValdiationService valdiationService;

    private ContactResponse toContactResponse(Contact contact) {
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

        return toContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findByUserAndId(user, id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request) {
        valdiationService.validate(request);

        Contact contact = contactRepository.findByUserAndId(user, request.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstname(request.getFirstname());
        contact.setLastname(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public void delete(User user, String id) {
        Contact contact = contactRepository.findByUserAndId(user, id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(builder.equal(root.get("user"), user));

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                    builder.like(root.get("firstname"), "%" + request.getName() + "%"),
                    builder.like(root.get("lastname"), "%" + request.getName() + "%")
                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        
        // Cara 1
        // List<ContactResponse> contactResponses = contacts.getContent().stream()
        //     .map(contact -> toContactResponse(contact))
        //     .collect(Collectors.toList());

        // Cara 2
        List<ContactResponse> contactResponses = contacts.getContent().stream().map(this::toContactResponse).toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
