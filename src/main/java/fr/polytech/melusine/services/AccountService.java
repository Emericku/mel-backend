package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordService passwordService;
    private final Clock clock;

    public AccountService(AccountRepository accountRepository, PasswordService passwordService,
                          Clock clock) {
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
        this.clock = clock;
    }

    /**
     * Create an account.
     *
     * @param accountRequest
     * @return
     */
    public void createAccount(AccountRequest accountRequest) {
        String encryptedPassword = passwordService.encryptPassword(accountRequest.getPassword().trim());

        Account account = Account.builder()
                .email(accountRequest.getEmail().trim().toLowerCase())
                .password(encryptedPassword)
                .isBarman(accountRequest.isBarman())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        accountRepository.save(account);
    }

    /**
     * Update an account.
     *
     * @param id
     * @param accountRequest
     */
    public void updateAccount(String id, AccountRequest accountRequest) {
        log.debug("Update account with client ID: {} ", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AccountError.INVALID_CLIENT_ID, id));

        String requestedEmail = accountRequest.getEmail().trim().toLowerCase();
        boolean emailAlreadyExists = accountRepository.existsByEmail(requestedEmail);
        if (emailAlreadyExists) {
            throw new ConflictException(AccountError.CONFLICT_EMAIL, requestedEmail);
        }

        Account updatedAccount = account.toBuilder()
                .email(accountRequest.getEmail())
                .isBarman(accountRequest.isBarman())
                .build();

        accountRepository.save(updatedAccount);
    }

}
