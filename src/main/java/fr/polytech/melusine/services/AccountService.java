package fr.polytech.melusine.services;

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

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
        if (emailAlreadyExists) throw new ConflictException(AccountError.CONFLICT_EMAIL, requestedEmail);

        Account updatedAccount = account.toBuilder()
                .email(accountRequest.getEmail())
                .isBarman(accountRequest.isBarman())
                .build();

        accountRepository.save(updatedAccount);
    }

}
