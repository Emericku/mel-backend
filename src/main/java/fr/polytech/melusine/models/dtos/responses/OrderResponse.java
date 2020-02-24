package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderResponse {

    @NonNull
    private String id;

    @NonNull
    private String clientName;

    private double total;

    @NonNull
    private OrderStatus status;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
