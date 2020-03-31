package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    /**
     * Check if a user exists by his first name, last name and section.
     *
     * @param firstName the first name
     * @param LastName  the last nam
     * @param section   the section
     * @return a boolean
     */
    boolean existsByFirstNameAndLastNameAndSection(String firstName, String LastName, Section section);

    /**
     * find user by his first name or last name or nick name containing the char.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param nickName  the nick name
     * @return an optional user
     */
    Page<User> findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrNickNameIgnoreCaseContaining(Pageable pageable, String firstName, String lastName, String nickName);

    @Override
    List<User> findAll();

}
