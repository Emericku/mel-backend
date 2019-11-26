package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
@Builder
public class AccountRequest {

    @NonNull
    @Size(min = 8)
    private String password;

    @NonNull
    private String email;

    private boolean isBarman;

}
