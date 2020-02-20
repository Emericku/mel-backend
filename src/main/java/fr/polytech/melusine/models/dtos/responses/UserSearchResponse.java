package fr.polytech.melusine.models.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String nickName;

    private double credit;

}
