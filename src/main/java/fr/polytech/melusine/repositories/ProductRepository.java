package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
    /**
     * Find a product by name.
     *
     * @param name
     * @return
     */
    Optional<Product> findByName(String name);

    /**
     * Check if a product exists by his name and if it's original.
     *
     * @param name
     * @return
     */
    boolean existsByNameAndIsOriginalTrue(String name);

}
