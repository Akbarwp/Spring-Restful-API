package springboot.restful.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import springboot.restful.entity.Address;
import springboot.restful.entity.Contact;
import springboot.restful.entity.User;
import springboot.restful.model.AddressResponse;
import springboot.restful.repository.AddressRepository;
import springboot.restful.repository.ContactRepository;
import springboot.restful.request.CreateAddressRequest;
import springboot.restful.request.UpdateAddressRequest;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValdiationService valdiationService;

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
            .id(address.getId())
            .street(address.getStreet())
            .city(address.getCity())
            .province(address.getProvince())
            .country(address.getCountry())
            .postalCode(address.getPostalCode())
            .createdAt(address.getCreatedAt())
            .updatedAt(address.getUpdatedAt())
            .build();
    }

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        valdiationService.validate(request);

        Contact contact = contactRepository.findByUserAndId(user, request.getContactId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);
        address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findByContactAndId(contact, addressId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        valdiationService.validate(request);

        Contact contact = contactRepository.findByUserAndId(user, request.getContactId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findByContactAndId(contact, request.getAddressId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);
        address.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional
    public void delete(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findByContactAndId(contact, addressId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        
        List<Address> addresses = addressRepository.findAllByContact(contact);

        // Cara 1
        // return addresses.stream().map(address -> toAddressResponse(address)).collect(Collectors.toList());

        // Cara 2
        return addresses.stream().map(this::toAddressResponse).toList();
    }
}
