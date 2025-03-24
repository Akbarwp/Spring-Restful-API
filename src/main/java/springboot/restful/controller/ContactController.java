package springboot.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.ContactResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.CreateContactRequest;
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
}
