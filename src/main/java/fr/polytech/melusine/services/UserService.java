package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.dtos.requests.UserRegistrationRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.AccountRepository;
import fr.polytech.melusine.repositories.UserRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

import static fr.polytech.melusine.utils.PriceFormatter.formatToLong;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private PasswordService passwordService;
    private Clock clock;


    public UserService(UserRepository userRepository, AccountRepository accountRepository, PasswordService passwordService, Clock clock) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
        this.clock = clock;
    }

    public User createUser(UserRegistrationRequest userRegistrationRequest) {
        log.info("Creation of user with last name: " + userRegistrationRequest.getLastName() +
                " first name: " + userRegistrationRequest.getFirstName());
        ensureCreditUpperThanZero(userRegistrationRequest.getCredit());
        String firstName = Strings.capitalize(userRegistrationRequest.getFirstName().toLowerCase().trim());
        String lastName = Strings.capitalize(userRegistrationRequest.getLastName().toLowerCase().trim());
        String nickName = Strings.capitalize(userRegistrationRequest.getNickName().toLowerCase().trim());
        if (userRepository.existsByFirstNameAndLastNameAndSection(firstName, lastName, userRegistrationRequest.getSection()))
            throw new ConflictException(UserError.CONFLICT, firstName, lastName, userRegistrationRequest.getSection());

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName)
                .section(userRegistrationRequest.getSection())
                .credit(userRegistrationRequest.getCredit())
                .isMembership(userRegistrationRequest.isMembership())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        User savedUser = userRepository.save(user);

        AccountRequest accountRequest = userRegistrationRequest.getAccount();
        if (Objects.nonNull(accountRequest)) {
            String encryptedPassword = passwordService.encryptPassword(accountRequest.getPassword().trim());
            String email = accountRequest.getEmail().trim().toLowerCase();
            if (accountRepository.existsByEmail(email))
                throw new ConflictException(AccountError.CONFLICT_EMAIL, email);

            Account account = Account.builder()
                    .password(encryptedPassword)
                    .email(email)
                    .isBarman(accountRequest.isBarman())
                    .user(savedUser)
                    .createdAt(OffsetDateTime.now(clock))
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();
            accountRepository.save(account);
        }
        log.info("Creation success");
        return user;
    }

    private void ensureCreditUpperThanZero(Long credit) {
        if (credit <= 0) throw new BadRequestException(CreditError.INVALID_CREDIT, credit);
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

    public User creditUser(String userId, double amount) {
        log.debug("Credit a user with ID : " + userId + " and amount : " + amount);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, userId));

        long requestedCredit = formatToLong(amount);
        long newCredit = user.getCredit() + requestedCredit;
        User updatedUser = user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        log.info("End of credit a user");
        return userRepository.save(updatedUser);
    }

}
