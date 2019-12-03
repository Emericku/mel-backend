package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, String> {

    /**
     * Find ingredient by name.
     *
     * @param name
     * @return
     */
    Optional<Ingredient> findByName(String name);

    boolean existsByName(String name);

}
