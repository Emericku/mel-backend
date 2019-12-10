package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.UserSearchResponse;
import fr.polytech.melusine.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSearchResponse mapToUserSearchResponse(User user) {
        return UserSearchResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickName(user.getNickName())
                .credit(user.getCredit())
                .build();
    }

}
