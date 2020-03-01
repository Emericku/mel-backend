package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {

    /**
     * Find account by the email.
     *
     * @param email the email
     * @return an optional account
     */
    Optional<Account> findByEmail(String email);

    /**
     * Find account by client id.
     *
     * @param id the account id
     * @return an optional account
     */
    @Override
    Optional<Account> findById(String id);

    /**
     * Return true if an account exists with the current email.
     *
     * @param email the email
     * @return a boolean
     */
    boolean existsByEmail(String email);

    void deleteByUser(User user);

}
