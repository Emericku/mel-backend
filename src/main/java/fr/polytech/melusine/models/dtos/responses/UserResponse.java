package fr.polytech.melusine.models.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserResponse {

    private String firstName;

    private String lastName;

    private String nickName;

    private long credit;

    private boolean isMembership;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
