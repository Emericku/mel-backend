package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, String> {

    /**
     * Find ingredient by name.
     *
     * @param name
     * @return
     */
    Optional<Ingredient> findByName(String name);

    /**
     * Check if the ingredient exists by his name.
     *
     * @param name the name
     * @return a boolean
     */
    boolean existsByName(String name);

    /**
     * Find all ingredients.
     *
     * @return a list of ingredients
     */
    @Override
    List<Ingredient> findAll();

    List<Ingredient> findByIdIn(List<String> ids);

}
