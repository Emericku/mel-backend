package fr.polytech.melusine.services;

import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.AccountRepository;
import fr.polytech.melusine.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private PasswordService passwordService;
    private EmailManager emailManager;
    private Clock clock;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, PasswordService passwordService, EmailManager emailManager, Clock clock) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.emailManager = emailManager;
        this.clock = clock;
    }

    /**
     * Update an account.
     *
     * @param accountRequest the request
     * @return the account
     */
    public Account updateAccount(AccountRequest accountRequest) {
        log.debug("Update account with client ID: {} ", accountRequest.getClientId());
        if (Objects.nonNull(accountRequest.getClientId())) {
            String email = accountRequest.getEmail().trim().toLowerCase();
            User user = userRepository.findById(accountRequest.getClientId())
                    .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, accountRequest.getClientId()));
            Account account = accountRepository.findByUser(user)
                    .orElseThrow(() -> new NotFoundException(AccountError.INVALID_CLIENT_ID, user.getId()));

            if (accountRepository.existsByEmailAndUserIsNot(email, user)) {
                throw new ConflictException(AccountError.CONFLICT_EMAIL, email);
            }

            Account updatedAccount = account.toBuilder()
                    .email(email)
                    .isBarman(accountRequest.isBarman())
                    .password(Objects.nonNull(accountRequest.getPassword()) ? passwordService.encryptPassword(accountRequest.getPassword().trim()) : account.getPassword())
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();

            return accountRepository.save(updatedAccount);
        }
        throw new BadRequestException(AccountError.INVALID_CLIENT_ID, accountRequest.getClientId());
    }

    public Account createAccount(AccountRequest accountRequest) {
        log.debug("Create account with client ID: {} ", accountRequest.getClientId());
        if (Objects.nonNull(accountRequest.getClientId()) &&
                Objects.nonNull(accountRequest.getPassword())) {
            String email = accountRequest.getEmail().trim().toLowerCase();

            User user = userRepository.findById(accountRequest.getClientId())
                    .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, accountRequest.getClientId()));

            if (accountRepository.existsByEmailAndUserIsNot(email, user)) {
                throw new ConflictException(AccountError.CONFLICT_EMAIL, email);
            }

            String encryptedPassword = passwordService.encryptPassword(accountRequest.getPassword().trim());
            Account updatedAccount = Account.builder()
                    .email(email)
                    .isBarman(accountRequest.isBarman())
                    .password(encryptedPassword)
                    .user(user)
                    .createdAt(OffsetDateTime.now(clock))
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();

            return accountRepository.save(updatedAccount);
        }
        throw new BadRequestException(AccountError.INVALID_CLIENT_ID, accountRequest.getClientId());
    }

    /**
     * Resend the password via email.
     *
     * @param email the email.
     */
    public void resendPassword(String email) {
        log.debug("Resend password for email {}", email);
        emailManager.resendPassword(email, "blarf");
    }

}
