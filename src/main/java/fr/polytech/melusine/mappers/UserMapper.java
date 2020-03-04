package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.entities.User;
import org.springframework.stereotype.Component;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;

@Component
public class UserMapper {

    public UserResponse mapToUserResponse(User user, String email, Boolean isBarman) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickName(user.getNickName())
                .credit(formatToDouble(user.getCredit()))
                .section(user.getSection())
                .isMembership((user.isMembership()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isBarman(isBarman)
                .email(email)
                .build();
    }

}
