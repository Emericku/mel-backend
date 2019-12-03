package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, String> {
}
