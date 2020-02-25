package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderItemRequest {

    @NotNull
    private OrderStatus status;

}
