package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.ValidationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequest {

    private ValidationStatus status;

}
