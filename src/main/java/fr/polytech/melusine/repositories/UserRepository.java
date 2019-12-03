package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.Section;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    /**
     * Check if a user exists by his first name, last name and section.
     *
     * @param firstName
     * @param LastName
     * @param section
     * @return
     */
    boolean existsByFirstNameAndLastNameAndSection(String firstName, String LastName, Section section);

}
