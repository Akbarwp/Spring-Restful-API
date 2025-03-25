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
import springboot.restful.model.ContactResponse;
import springboot.restful.model.PagingResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.CreateContactRequest;
import springboot.restful.request.SearchContactRequest;
import springboot.restful.request.UpdateContactRequest;
import springboot.restful.service.ContactService;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
        path = "/contacts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);

        return WebResponse.<ContactResponse> builder()
            .messages("Create contact success")
            .data(contactResponse)
            .build();
    }

    @GetMapping(
        path = "/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user, @PathVariable(value = "contactId") String contactId) {
        ContactResponse contactResponse = contactService.get(user, contactId);

        return WebResponse.<ContactResponse> builder()
            .messages("Get contact success")
            .data(contactResponse)
            .build();
    }

    @PutMapping(
        path = "/contacts/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(User user, @RequestBody UpdateContactRequest request, @PathVariable(value = "contactId") String contactId) {
        request.setId(contactId);
        ContactResponse contactResponse = contactService.update(user, request);

        return WebResponse.<ContactResponse> builder()
            .messages("Update contact success")
            .data(contactResponse)
            .build();
    }

    @DeleteMapping(
        path = "/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable(value = "contactId") String contactId) {
        contactService.delete(user, contactId);

        return WebResponse.<String> builder()
            .messages("Delete contact success")
            .build();
    }

    @GetMapping(
        path = "/contacts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> search(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
        ) {

        SearchContactRequest request = SearchContactRequest.builder()
            .name(name)
            .email(email)
            .phone(phone)
            .page(page)
            .size(size)
            .build();

        Page<ContactResponse> contactResponse = contactService.search(user, request);

        return WebResponse.<List<ContactResponse>> builder()
            .messages("Search contact success")
            .data(contactResponse.getContent())
            .paging(
                PagingResponse
                .builder()
                .currentPage(contactResponse.getNumber())
                .totalPage(contactResponse.getTotalPages())
                .size(contactResponse.getSize())
                .build()
            )
            .build();
    }
}
