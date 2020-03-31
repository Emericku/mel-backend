package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.requests.UserCsvRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.Section;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;
import static fr.polytech.melusine.utils.MoneyFormatter.formatToLong;

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

    public User mapToUser(UserCsvRequest userCsvRequest, Section section, double credit, Clock clock) {
        return User.builder()
                .firstName(Strings.capitalize(userCsvRequest.getPrenom().toLowerCase().trim()))
                .lastName(Strings.capitalize(userCsvRequest.getNom().toLowerCase().trim()))
                .nickName(Objects.nonNull(userCsvRequest.getSurnom()) ? Strings.capitalize(userCsvRequest.getSurnom().toLowerCase().trim()) : null)
                .section(section)
                .credit(formatToLong(credit))
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .isMembership(true)
                .build();
    }

}
