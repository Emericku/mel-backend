package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Find all item from an order.
     *
     * @return a page of item
     */
    Page<OrderItem> findAll(Pageable pageable);


    /**
     * Find all item by the status.
     *
     * @param pageable the pageable configuration
     * @param status   the status
     * @return a page of OrderItem
     */
    Page<OrderItem> findAllByStatus(Pageable pageable, OrderStatus status);

}
