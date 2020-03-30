package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.charts.OrderPoint;
import fr.polytech.melusine.models.dtos.responses.OrderResponse;
import fr.polytech.melusine.models.entities.Order;
import org.springframework.stereotype.Component;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;

@Component
public class OrderMapper {

    public OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .clientName(order.getClientName())
                .status(order.getStatus())
                .total(formatToDouble(order.getTotal()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public OrderPoint mapToOrderPoint(Order order) {
        return OrderPoint.builder()
                .ordinate(order.getTotal())
                .abscissa(order.getUpdatedAt())
                .build();
    }

}
