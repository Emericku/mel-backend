package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.dtos.responses.UserSearchResponse;
import fr.polytech.melusine.models.entities.User;
import org.springframework.stereotype.Component;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;

@Component
public class UserMapper {

    public UserSearchResponse mapToUserSearchResponse(User user) {
        return UserSearchResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickName(user.getNickName())
                .credit(formatToDouble(user.getCredit()))
                .build();
    }

    public UserResponse mapToUserResponse(User user) {
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
                .build();
    }

}
