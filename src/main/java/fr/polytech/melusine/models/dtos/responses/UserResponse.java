package fr.polytech.melusine.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponse {

    private String firstName;

    private String lastName;

    private String nickName;

    private long credit;

    private boolean isMembership;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
