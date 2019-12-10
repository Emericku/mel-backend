package fr.polytech.melusine.models.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponse {

    private String ingredientId;

    private String name;

    private long price;

    private long quantity;

    private String image;

}
