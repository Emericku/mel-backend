package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class AccountRequest {

    private String clientId;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

    boolean isBarman;

}
