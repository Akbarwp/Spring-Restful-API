package springboot.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.AddressResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.CreateAddressRequest;
import springboot.restful.request.UpdateAddressRequest;
import springboot.restful.service.AddressService;


@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
        path = "/contacts/{contactId}/addresses",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(
        User user,
        @RequestBody CreateAddressRequest request,
        @PathVariable(value = "contactId") String contactId
    ) {

        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);

        return WebResponse.<AddressResponse> builder()
            .messages("Create address success")
            .data(addressResponse)
            .build();
    }

    @GetMapping(
        path = "/contacts/{contactId}/addresses/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(
        User user,
        @PathVariable(value = "contactId") String contactId,
        @PathVariable(value = "addressId") String addressId
    ) {

        AddressResponse addressResponse = addressService.get(user, contactId, addressId);

        return WebResponse.<AddressResponse> builder()
            .messages("Get address success")
            .data(addressResponse)
            .build();
    }

    @PutMapping(
        path = "/contacts/{contactId}/addresses/{addressId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(
        User user,
        @RequestBody UpdateAddressRequest request,
        @PathVariable(value = "contactId") String contactId,
        @PathVariable(value = "addressId") String addressId
    ) {

        request.setContactId(contactId);
        request.setAddressId(addressId);
        AddressResponse addressResponse = addressService.update(user, request);

        return WebResponse.<AddressResponse> builder()
            .messages("Update address success")
            .data(addressResponse)
            .build();
    }

    @DeleteMapping(
        path = "/contacts/{contactId}/addresses/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
        User user,
        @PathVariable(value = "contactId") String contactId,
        @PathVariable(value = "addressId") String addressId
    ) {

        addressService.delete(user, contactId, addressId);

        return WebResponse.<String> builder()
            .messages("Delete address success")
            .build();
    }

    @GetMapping(
        path = "/contacts/{contactId}/addresses",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> list(
        User user,
        @PathVariable(value = "contactId") String contactId
    ) {

        List<AddressResponse> addressResponse = addressService.list(user, contactId);

        return WebResponse.<List<AddressResponse>> builder()
            .messages("Get address success")
            .data(addressResponse)
            .build();
    }
}
