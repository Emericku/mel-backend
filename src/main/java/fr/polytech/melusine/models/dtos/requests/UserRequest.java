package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserRequest {

    @NonNull
    private String password;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String nickName;

    @NonNull
    private long credit;

    private boolean isMembership;

}
