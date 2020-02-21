package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.OrderItemResponse;
import fr.polytech.melusine.models.entities.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem, String name) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .name(name)
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .orderId(orderItem.getOrder().getId())
                .status(orderItem.getStatus())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }

}
