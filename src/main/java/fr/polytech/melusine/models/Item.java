package fr.polytech.melusine.models;

import lombok.Builder;
import lombok.Data;

/**
 * The item of an order.
 */
@Data
@Builder
public class Item {

    private String productId;

    private int quantity;

}
