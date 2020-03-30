package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.charts.OrderItemPoint;
import fr.polytech.melusine.models.dtos.responses.OrderItemResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;

@Component
public class OrderItemMapper {

    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productName(orderItem.getProduct().getName())
                .clientName(orderItem.getOrder().getClientName())
                .ingredients(orderItem.getProduct().getIngredients().stream().map(Ingredient::getName).distinct().collect(Collectors.toList()))
                .price(formatToDouble(orderItem.getPrice()))
                .orderId(orderItem.getOrder().getId())
                .status(orderItem.getStatus())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }

    public OrderItemPoint mapToOrderItemPoint(OrderItem orderItem) {
        return OrderItemPoint.builder()
                .ordinate(orderItem.getUpdatedAt())
                .abscissa(orderItem.getProduct().getName())
                .build();
    }

}
