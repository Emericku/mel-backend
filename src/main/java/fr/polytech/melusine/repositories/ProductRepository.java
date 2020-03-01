package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.entities.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {

    Optional<Product> findByIdAndIsDeletedFalse(String id);

    /**
     * Check if a product exists by his name and if it's original.
     *
     * @param name
     * @return
     */
    boolean existsByNameAndIsOriginalTrueAndIsDeletedFalse(String name);

    /**
     * Find product by original true.
     *
     * @return a list of product
     */
    List<Product> findByIsOriginalTrueAndIsDeletedFalse();

    List<Product> findByIngredientsInAndIsDeletedFalse(List<Ingredient> ingredient);


}
