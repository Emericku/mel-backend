package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends CrudRepository<OrderItem, String> {

    /**
     * Find and order item by his id and order id.
     *
     * @param id      the id
     * @param orderId the order id
     * @return an optional order item
     */
    Optional<OrderItem> findByIdAndOrderId(String id, String orderId);

    /**
     * Find all order items by his order id.
     *
     * @param orderId the order id
     * @return a list of order item
     */
    List<OrderItem> findAllByOrderId(String orderId);


    boolean existsByOrderIdAndCancelledFalseOrDeliveredFalse(String orderId);

}
