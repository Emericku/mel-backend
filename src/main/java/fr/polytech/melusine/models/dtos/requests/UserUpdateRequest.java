package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.Section;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private Double credit;

    private String firstName;

    private String lastName;

    private String nickName;

    private Section section;

}
