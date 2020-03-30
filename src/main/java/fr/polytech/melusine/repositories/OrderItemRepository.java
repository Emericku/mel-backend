package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.enums.Category;
import fr.polytech.melusine.models.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, String> {

    /**
     * Find all item by the status.
     *
     * @param pageable the pageable configuration
     * @param status   the status
     * @return a page of OrderItem
     */
    Page<OrderItem> findAllByStatus(Pageable pageable, OrderStatus status);


    List<OrderItem> findByCreatedAtBetweenAndStatusAndProductCategoryIsIn(OffsetDateTime start, OffsetDateTime now, OrderStatus status, List<Category> categories);

}
