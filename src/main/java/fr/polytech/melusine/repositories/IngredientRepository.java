package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.enums.IngredientType;
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

    List<Ingredient> findAllByTypeIsNot(IngredientType type);

    Ingredient findByName(String name);

}
