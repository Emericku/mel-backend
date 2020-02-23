package fr.polytech.melusine.services;

import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private EmailManager emailManager;

    public AccountService(AccountRepository accountRepository, EmailManager emailManager) {
        this.accountRepository = accountRepository;
        this.emailManager = emailManager;
    }

    /**
     * Update an account.
     *
     * @param accountRequest the request
     * @return the account
     */
    public Account updateAccount(AccountRequest accountRequest) {
        log.debug("Update account with client ID: {} ", accountRequest.getClientId());

        Account account = accountRepository.findById(accountRequest.getClientId())
                .orElseThrow(() -> new NotFoundException(AccountError.INVALID_CLIENT_ID, accountRequest.getClientId()));

        String requestedEmail = accountRequest.getEmail().trim().toLowerCase();
        boolean emailAlreadyExists = accountRepository.existsByEmail(requestedEmail);

        if (emailAlreadyExists) {
            throw new ConflictException(AccountError.CONFLICT_EMAIL, requestedEmail);
        }

        Account updatedAccount = account.toBuilder()
                .email(accountRequest.getEmail())
                .isBarman(accountRequest.isBarman())
                .build();

        return accountRepository.save(updatedAccount);
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
