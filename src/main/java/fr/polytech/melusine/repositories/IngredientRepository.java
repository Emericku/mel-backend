package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.enums.IngredientType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, String> {

    /**
     * Find all ingredients.
     *
     * @return a list of ingredients
     */
    List<Ingredient> findByIsDeletedFalse();

    List<Ingredient> findByIdInAndIsDeletedFalse(List<String> ids);

    List<Ingredient> findAllByTypeIsNotAndIsDeletedFalse(IngredientType type);

    Optional<Ingredient> findByIdAndIsDeletedFalse(String id);

}
