package fr.polytech.melusine.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

    private String productId;

    private int quantity;

}
