package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.mappers.UserMapper;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.dtos.requests.UserRegistrationRequest;
import fr.polytech.melusine.models.dtos.requests.UserUpdateRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.AccountRepository;
import fr.polytech.melusine.repositories.OrderRepository;
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

import static fr.polytech.melusine.utils.MoneyFormatter.formatToLong;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private PasswordService passwordService;
    private UserMapper userMapper;
    private OrderRepository orderRepository;
    private Clock clock;


    public UserService(UserRepository userRepository, AccountRepository accountRepository, PasswordService passwordService,
                       UserMapper userMapper, OrderRepository orderRepository, Clock clock) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    public UserResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        log.debug("Creation of user with last name: " + userRegistrationRequest.getLastName() +
                " first name: " + userRegistrationRequest.getFirstName());
        ensureCreditUpperThanZero(formatToLong(userRegistrationRequest.getCredit()));
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
                .credit(formatToLong(userRegistrationRequest.getCredit()))
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
        log.info("End of the creation of a user");
        return userMapper.mapToUserResponse(savedUser);
    }

    private void ensureCreditUpperThanZero(Long credit) {
        if (credit <= 0) throw new BadRequestException(CreditError.INVALID_CREDIT, credit);
    }

    /**
     * Get all users by page.
     *
     * @param pageable the page
     * @return a page object of user response
     */
    public Page<UserResponse> getUsers(Pageable pageable) {
        log.debug("Find accounts order by last name");
        Page<User> userPages = userRepository.findAll(pageable);
        return userPages.map(userPage -> userMapper.mapToUserResponse(userPage));
    }

    public UserResponse creditUser(String userId, UserUpdateRequest request) {
        log.debug("Credit a user with ID : " + userId + " and amount : " + request.getCredit());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, userId));

        long requestedCredit = formatToLong(request.getCredit());
        long newCredit = user.getCredit() + requestedCredit;
        User updatedUser = userRepository.save(user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build()
        );

        log.info("End of credit a user");
        return userMapper.mapToUserResponse(updatedUser);
    }

    public Page<UserResponse> searchUser(String name, Pageable pageable) {
        log.debug("Search user by this char : " + name);
        String formattedName = name.toLowerCase().trim();
        Page<User> users = userRepository.findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrNickNameIgnoreCaseContaining(
                pageable,
                formattedName,
                formattedName,
                formattedName
        );
        return users.map(user -> userMapper.mapToUserResponse(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, id));
        String firstName = Strings.capitalize(request.getFirstName().toLowerCase().trim());
        String lastName = Strings.capitalize(request.getLastName().toLowerCase().trim());
        String nickName = Strings.capitalize(request.getNickName().toLowerCase().trim());
        User updatedUser = user.toBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName)
                .section(request.getSection())
                .build();

        User savedUser = userRepository.save(updatedUser);
        return userMapper.mapToUserResponse(savedUser);
    }

    public void deleteUser(String id) {
        log.info("Deletion of order for user with ID: " + id);
        orderRepository.deleteByUserId(id);
        log.info("Deletion of user with ID:" + id);
        userRepository.deleteById(id);
    }

}
