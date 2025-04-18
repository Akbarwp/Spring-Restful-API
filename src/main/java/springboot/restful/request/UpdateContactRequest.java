package springboot.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
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
public class UpdateContactRequest {

    @JsonIgnore
    @NotBlank
    private String id;

    @NotBlank
    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 15)
    private String phone;
}
