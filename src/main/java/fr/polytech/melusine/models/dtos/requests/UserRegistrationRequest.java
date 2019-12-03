package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.Section;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserRegistrationRequest {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String nickName;

    @NonNull
    private Section section;

    private long credit;

    private boolean isMembership;

    private AccountRequest account;

}
