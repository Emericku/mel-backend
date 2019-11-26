package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.models.dtos.requests.UserRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.UserRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private Clock clock;


    public UserService(UserRepository userRepository, Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public void createUser(UserRequest userRequest) {
        log.info("Creation of user with last name: " + userRequest.getLastName() +
                " first name: " + userRequest.getFirstName());
        ensureCreditUpperThanZero(userRequest.getCredit());
        String firstName = Strings.capitalize(userRequest.getFirstName().toLowerCase().trim());
        String lastName = Strings.capitalize(userRequest.getLastName().toLowerCase().trim());
        String nickName = Strings.capitalize(userRequest.getNickName().toLowerCase().trim());

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName)
                .credit(userRequest.getCredit())
                .isMembership(userRequest.isMembership())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        userRepository.save(user);
    }

    private void ensureCreditUpperThanZero(Long credit) {
        if (credit <= 0) {
            throw new BadRequestException(CreditError.INVALID_CREDIT, credit);
        }
    }

    /**
     * Get all accounts by page.
     *
     * @param pageable
     * @return
     */
    public Page<UserResponse> getUsers(Pageable pageable) {
        log.debug("Find accounts order by last name");
        Page<User> userPages = userRepository.findAll(pageable);
        return userPages.map(userPage -> UserResponse.builder()
                .firstName(userPage.getFirstName())
                .lastName(userPage.getLastName())
                .nickName(userPage.getNickName())
                .credit(userPage.getCredit())
                .isMembership(userPage.isMembership())
                .updatedAt(userPage.getUpdatedAt())
                .createdAt(userPage.getCreatedAt())
                .build()
        );
    }

}
