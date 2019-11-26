package fr.polytech.melusine.models.dtos.requests;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;

    private String password;

}
