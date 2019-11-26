package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
