package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.Section;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String nickName;

    private Section section;

    private double credit;

    private boolean isMembership;

    private boolean isBarman;

    private String email;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
