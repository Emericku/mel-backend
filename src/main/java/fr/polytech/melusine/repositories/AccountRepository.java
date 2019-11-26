package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {

    /**
     * Find account by the username.
     *
     * @param username
     * @return
     */
    Optional<Account> findByEmail(String username);

    /**
     * Find account by client id.
     *
     * @param id
     * @return
     */
    Optional<Account> findById(String id);

    /**
     * Return true if an account exists with the current email.
     *
     * @param email
     * @return
     */
    boolean existsByEmail(String email);

}
