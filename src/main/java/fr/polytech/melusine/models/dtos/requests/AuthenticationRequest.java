package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AuthenticationRequest {

    @NonNull
    private String email;

    @NonNull
    private String password;

}
