package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.Section;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserRegistrationRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String nickName;

    @NotNull
    private Section section;

    private double credit;

    private boolean isMembership;

    private AccountRequest account;

}
