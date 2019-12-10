package fr.polytech.melusine.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemResponse {

    private String id;

    private String name;

    private long price;

    private long quantity;

    private String orderId;

    private boolean delivered;

    private boolean cancelled;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
