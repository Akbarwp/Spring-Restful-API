package springboot.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {

    @JsonIgnore
    @NotBlank
    private String contactId;

    @Size(max = 255)
    private String street;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String province;

    @NotBlank
    @Size(max = 255)
    private String country;

    @NotBlank
    @Size(max = 5)
    private String postalCode;
}
