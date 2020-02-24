package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class OrderItemResponse {

    private String id;

    private String orderId;

    private String productName;

    private String clientName;

    private List<String> ingredients;

    private double price;

    private OrderStatus status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
