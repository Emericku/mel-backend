package fr.polytech.melusine.repositories;

import fr.polytech.melusine.models.entities.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {

    void deleteByUserId(String id);

}
