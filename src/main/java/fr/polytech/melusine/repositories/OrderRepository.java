package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.OrderStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {

    void deleteByUser(User user);

    List<Order> findByCreatedAtBetweenAndStatus(OffsetDateTime start, OffsetDateTime now, OrderStatus status);

}
