package fr.polytech.melusine.models.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private String accessToken;

    private String tokenType;

    private int expiresIn;

}
