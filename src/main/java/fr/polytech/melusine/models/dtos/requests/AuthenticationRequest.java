package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AuthenticationRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
