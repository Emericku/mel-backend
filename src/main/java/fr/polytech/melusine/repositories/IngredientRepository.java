package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, String> {

    /**
     * Find all ingredients.
     *
     * @return a list of ingredients
     */
    @Override
    List<Ingredient> findAll();

    List<Ingredient> findByIdIn(List<String> ids);

    Ingredient findByName(String name);

}
