package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.ValidationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderItemResponse {

    private String id;

    private String name;

    private long price;

    private long quantity;

    private String orderId;

    private ValidationStatus status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
