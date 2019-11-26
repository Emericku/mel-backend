package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
}
